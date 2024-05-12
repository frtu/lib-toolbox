package com.github.frtu.kotlin.grpc.instrumentation

import com.fasterxml.jackson.core.io.JsonStringEncoder
import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.util.JsonFormat
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

class ProtoMethodParamLogger {
    private val jsonPrinter = JsonFormat.printer().includingDefaultValueFields().preservingProtoFieldNames()

    fun <M> log(grpcEventEnum: GrpcEventEnum, methodName: String, message: M) {
        if (isLogEnabled(grpcEventEnum, methodName)) {
            try {
                val messageInJson = toJSON(message)
                logger.info("{\"method_name\":\"{}\", \"{}\":{}}", methodName, grpcEventEnum.label, messageInJson)
            } catch (e: Exception) {
                // Fail without impacting execution flow.
                val errorMessage = e.message
                if (StringUtils.isNotBlank(errorMessage) && errorMessage!!.contains("mergeFrom(")) {
                    // Known exception when JAR library doesn't match !
                    logger.error("ATTENTION, Message:{}", errorMessage)
                } else {
                    logger.warn("Exception when serializing payload in JSON. Message:{}", errorMessage, e)
                }
            }
        }
    }

    fun <M> toJSON(message: M): String = if (message is MessageOrBuilder) {
        jsonPrinter.print(message)
    } else {
        val chars = JsonStringEncoder.getInstance().quoteAsString(message.toString().trim { it <= ' ' })
        StringBuilder().append('"').append(chars).append('"').toString()
    }

    fun isLogEnabled(grpcEventEnum: GrpcEventEnum, methodName: String): Boolean {
        when (grpcEventEnum) {
            GrpcEventEnum.SERVER_REQUEST, GrpcEventEnum.SERVER_RESPONSE -> return logger.isInfoEnabled
            else -> false
        }
        // Use DEBUG for the other case
        return logger.isDebugEnabled
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}