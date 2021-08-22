package com.github.frtu.kotlin.utils

import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader
import java.io.File

/**
 * Allow to easily read data from resources
 *
 * @author Frédéric TU
 * @since 1.1.3
 */
open class ResourceHelper {
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
}