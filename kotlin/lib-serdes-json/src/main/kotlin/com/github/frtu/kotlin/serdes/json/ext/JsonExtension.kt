package com.github.frtu.kotlin.serdes.json.ext

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.serdes.json.ext.JsonExtension.jsonNodeToObj
import com.github.frtu.kotlin.serdes.json.ext.JsonExtension.jsonToBean

object JsonExtension {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    fun toJsonString(obj: Any, pretty: Boolean): String = if (pretty) {
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
    } else {
        objectMapper.writeValueAsString(obj)
    }

    fun <T> jsonToBean(payload: String, clazz: Class<T>): T = objectMapper.readValue(payload, clazz)

    fun objToJsonNode(obj: Any): JsonNode = objectMapper.valueToTree(obj)
    fun <T> jsonNodeToObj(jsonNode: JsonNode, clazz: Class<T>): T = objectMapper.treeToValue(jsonNode, clazz)
}

fun Any.objToJsonNode(): JsonNode = JsonExtension.objToJsonNode(this)
fun <T> JsonNode.toJsonObj(clazz: Class<T>): T = jsonNodeToObj(this, clazz)

fun Any.toJsonString(pretty: Boolean = false): String = JsonExtension.toJsonString(this, pretty)
fun <T> String.toJsonObj(clazz: Class<T>): T = jsonToBean(this, clazz)

fun JsonNode.toJsonString(pretty: Boolean = false): String = if (pretty) toPrettyString() else toString()
fun String.toJsonNode(): JsonNode = toJsonObj(JsonNode::class.java)
