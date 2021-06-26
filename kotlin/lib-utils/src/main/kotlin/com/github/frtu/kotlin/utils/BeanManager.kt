package com.github.frtu.kotlin.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader

/**
 * Allow to easily deserialize an object and use object.
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
class BeanManager {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val resourceLoader = DefaultResourceLoader()
    private val mapper = jacksonObjectMapper()

    /**
     * Allow to easily deserialize an object using a class and a JSON file location.
     */
    fun <T> toBean(clazz: Class<T>, location: String): T? {
        logger.debug("Deserialize class:${clazz} from location:${location}")
        val resource = resourceLoader.getResource(location)
        return mapper.readValue(resource.file, clazz)
    }

    /**
     * Smart cast from List
     */
    fun toStringOrNull(list: List<Any?>, index: Int): String? {
        val result = toStringOrNull(list[index])
        logger.debug("Fetch index:${index} with result:${result}")
        return result
    }

    /**
     * Smart cast from Map
     */
    fun <T> toStringOrNull(map: Map<T, Any?>, key: T): String? {
        val result = toStringOrNull(map[key])
        logger.debug("Fetch key:${key} with result:${result}")
        return result
    }

    /**
     * Smart cast from Any
     */
    fun toStringOrNull(item: Any?): String? =
        if (item is String?) {
            item
        } else {
            null
        }
}