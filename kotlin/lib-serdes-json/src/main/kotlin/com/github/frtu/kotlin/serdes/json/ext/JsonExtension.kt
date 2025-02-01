package com.github.frtu.kotlin.serdes.json.ext

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.frtu.kotlin.serdes.json.ext.JsonExtension.jsonNodeToObj
import com.github.frtu.kotlin.serdes.json.ext.JsonExtension.jsonToBean

// JsonNode <-> Any
fun Any.objToJsonNode(): JsonNode = JsonExtension.objToJsonNode(this)
fun <T> JsonNode.toJsonObj(clazz: Class<T>): T = jsonNodeToObj(this, clazz)

// String <-> Any
fun Any.toJsonString(pretty: Boolean = false): String = JsonExtension.toJsonString(this, pretty)
fun <T> String.toJsonObj(clazz: Class<T>): T = jsonToBean(this, clazz)

// JsonNode <-> String
fun JsonNode.toJsonString(pretty: Boolean = false): String = if (pretty) toPrettyString() else toString()
fun String.toJsonNode(): JsonNode = toJsonObj(JsonNode::class.java)

object JsonExtension {
    fun toJsonString(obj: Any, pretty: Boolean): String = if (pretty) {
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
    } else {
        objectMapper.writeValueAsString(obj)
    }

    fun <T> jsonToBean(payload: String, clazz: Class<T>): T = objectMapper.readValue(payload, clazz)

    fun objToJsonNode(obj: Any): JsonNode = objectMapper.valueToTree(obj)
    fun <T> jsonNodeToObj(jsonNode: JsonNode, clazz: Class<T>): T = objectMapper.treeToValue(jsonNode, clazz)
}

val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
    // FAULT TOLERANT ON NULLABILITY AND ARRAYS
    // Leverage proper explicit Kotlin ? null value instead of defaulting to implicit NullNode.instance
    setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
    // Prevent errors when encountering extra fields in JSON
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    // Allow single object to be deserialized into list if required
    configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)

    // FACILITATE DATE
    // Set WRITE_DATES_AS_TIMESTAMPS to false for readable date formats.
    // Specify a fault-tolerant date format using setDateFormat().
    // Handle variations in input date formats using setDefaultLeniency(true).
    registerModule(JavaTimeModule())
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    //    dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") // ISO 8601 Format
    //    setDefaultLeniency(true) // Fault-tolerant date parsing

    // Others
    setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    registerModules(Jdk8Module(), ParameterNamesModule())
}