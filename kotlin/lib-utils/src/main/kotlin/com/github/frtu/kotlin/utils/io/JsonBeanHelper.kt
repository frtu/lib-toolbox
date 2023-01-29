package com.github.frtu.kotlin.utils.io

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.utils.io.JsonUtil.jsonToBean

/**
 * Allow to easily deserialize an object from external JSON source
 *
 * @author Frédéric TU
 * @since 1.1.4
 */
class JsonBeanHelper<T>(
    private val mapper: ObjectMapper = jacksonObjectMapper(),
    private val clazz: Class<T>? = null
) : BeanHelper() {
    /**
     * Allow to easily deserialize an object using a class and a JSON file location
     * using the clazz from constructor
     */
    fun toBean(location: String): T? = toBean(location, clazz!!)

    /**
     * Allow to easily deserialize an object using a class and a JSON file location.
     */
    fun <T> toBean(location: String, clazz: Class<T>): T? {
        logger.debug("Deserialize class:${clazz} from location:${location}")
        val resource = resourceLoader.getResource(location)
        return mapper.readValue(resource.file, clazz)
    }

    fun <T> jsonToBean(payload: String, clazz: Class<T>): T = mapper.readValue(payload, clazz)
}

object JsonUtil {
    private val objectMapper = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
    private val jsonBeanHelper = JsonBeanHelper<Any>(objectMapper)

    fun <T> jsonToBean(payload: String, clazz: Class<T>): T = jsonBeanHelper.jsonToBean(payload, clazz)

    /**
     * Write object into json string
     * @since 1.2.5
     */
    fun toJsonString(obj: Any): String =
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
}

fun String.toJsonNode() = toJsonObj(JsonNode::class.java)

fun <T> String.toJsonObj(clazz: Class<T>) = jsonToBean(this, clazz)

/**
 * Allow to print json from any object
 * @since 1.2.5
 */
fun Any.toJsonString() = JsonUtil.toJsonString(this)
