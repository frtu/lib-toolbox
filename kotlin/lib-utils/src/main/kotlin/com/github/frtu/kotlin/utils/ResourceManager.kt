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
class ResourceManager {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val resourceLoader = DefaultResourceLoader()
    private val mapper = jacksonObjectMapper()

    /**
     * Allow to easily deserialize an object using a class and a JSON file location.
     */
    fun <T> createFromFile(clazz: Class<T>, location: String): T? {
        logger.debug("Deserialize class:${clazz} from location:${location}")
        val resource = resourceLoader.getResource(location)
        return mapper.readValue(resource.file, clazz)
    }
}