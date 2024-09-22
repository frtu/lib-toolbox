package com.github.frtu.kotlin.llm.os.llm.model

import com.aallam.openai.api.chat.FunctionCall
import com.github.frtu.kotlin.llm.os.llm.model.InvokeFunction.Companion.logger
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class InvokeFunction(
    val name: String,
    val parameters: JsonArray? = null,
) {
    constructor(functionCall: FunctionCall, json: Json = Json) : this(
        name = functionCall.name,
        parameters = json.decodeFromString(functionCall.arguments),
    )

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}

fun parseContent(
    content: String,
    keyName: String = "FunctionName",
    keyParameters: String = "Parameters",
): InvokeFunction? = with(content) {
    logger.trace("Parsing content:$content")
    val startIndex = indexOfAny(listOf("[", "{"))
    val endIndex = lastIndexOfAny(listOf("]", "}")) + 1
    if (startIndex in 0..endIndex) {
        val jsonString = subSequence(startIndex, endIndex).toString()
        logger.trace("Extracted json:$jsonString")
        try {
            val jsonObject = Json.decodeFromString<JsonArray?>(jsonString)
            if (!jsonObject.isNullOrEmpty()) {
                return with(jsonObject.first().jsonObject) {
                    InvokeFunction(
                        name = getValue(keyName).jsonPrimitive.content,
                        parameters = getValue(keyParameters).jsonArray,
                    )
                }
            }
        } catch (e: SerializationException) {
            logger.warn("Deserialization error", e)
        }
    }
    return null
}