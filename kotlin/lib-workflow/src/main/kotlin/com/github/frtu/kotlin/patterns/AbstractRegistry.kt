package com.github.frtu.kotlin.patterns

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Registry of Element <T>
 * @author Frédéric TU
 * @since 1.1.4
 * @param <T> Type of element registered
 */
abstract class AbstractRegistry<T>(
    /** Type name for Log purpose */
    private val type: String,
    /** Storage for Element */
    private val registry: MutableMap<String, T> = mutableMapOf(),
) {
    open fun register(name: String, element: T): AbstractRegistry<T> {
        val normalizedName = normalizedName(name)
        logger.debug("register $type using normalizedName=$normalizedName")
        registry.putIfAbsent(normalizedName, element)
        return this
    }

    open fun getElement(name: String): T {
        val normalizedName = normalizedName(name)
        val result = registry[normalizedName] ?: throw UnrecognizedElementException(type, normalizedName)
        logger.debug("Resolve $type:[$name] with normalizedName=[$normalizedName] and result=[${result}]")
        return result
    }

    /**
     * Allow override name normalization using any convention
     */
    protected open fun normalizedName(name: String) = name

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
}