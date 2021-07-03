package com.github.frtu.kotlin.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Allow to easily deserialize an object from external JSON source
 *
 * @author Frédéric TU
 * @since 1.1.2
 */
class JsonBeanHelper<T>(
    private val mapper: ObjectMapper = jacksonObjectMapper(),
    private val clazz: Class<T>? = null
) : BeanHelper() {
    /**
     * Allow to easily deserialize an object using a class and a JSON file location
     * using the clazz from constructor
     */
    fun toBean(location: String): T? = toBean(clazz!!, location)

    /**
     * Allow to easily deserialize an object using a class and a JSON file location.
     */
    fun <T> toBean(clazz: Class<T>, location: String): T? {
        logger.debug("Deserialize class:${clazz} from location:${location}")
        val resource = resourceLoader.getResource(location)
        return mapper.readValue(resource.file, clazz)
    }
}