package com.github.frtu.kotlin.utils

import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader
import java.io.File

/**
 * Allow to easily deserialize an object and use object.
 *
 * @author Frédéric TU
 * @since 1.1.2
 */
open class BeanHelper {
    internal val logger = LoggerFactory.getLogger(this::class.java)
    internal val resourceLoader = DefaultResourceLoader()

    fun readFromFile(location: String): String? {
        val absolutePath = resourceLoader.getResource(location).file.absolutePath
        val file = File(absolutePath)
        return if (file.exists() && file.canRead()) {
            logger.debug("Reading file from location:$location & absolutePath:$absolutePath")
            val text = file.readText(Charsets.UTF_8)
            logger.debug("Deserialize text:{}", text)
            text
        } else {
            logger.warn("File doesn't exist or not readable at location:$location & absolutePath:$absolutePath")
            null
        }
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