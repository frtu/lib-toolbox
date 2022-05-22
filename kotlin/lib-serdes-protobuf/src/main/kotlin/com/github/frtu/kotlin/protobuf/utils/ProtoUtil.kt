package com.github.frtu.kotlin.protobuf.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.protobuf.utils.ProtoUtil.jsonToObj

object ProtoUtil {
    fun <T> jsonToObj(payload: String, clazz: Class<T>): T = objectMapper.readValue(payload, clazz)

    private val objectMapper: ObjectMapper =
        jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
}

fun String.toJsonNode() = toJsonObj(JsonNode::class.java)

fun <T> String.toJsonObj(clazz: Class<T>) = jsonToObj(this, clazz)
