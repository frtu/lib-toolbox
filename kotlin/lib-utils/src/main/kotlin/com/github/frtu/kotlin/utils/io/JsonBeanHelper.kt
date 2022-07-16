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
    fun <T> jsonToBean(payload: String, clazz: Class<T>): T = jsonBeanHelper.jsonToBean(payload, clazz)

    private val jsonBeanHelper =
        JsonBeanHelper<Any>(jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL))
}

fun String.toJsonNode() = toJsonObj(JsonNode::class.java)

fun <T> String.toJsonObj(clazz: Class<T>) = jsonToBean(this, clazz)
