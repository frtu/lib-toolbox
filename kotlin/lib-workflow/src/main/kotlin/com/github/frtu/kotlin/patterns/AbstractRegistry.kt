package com.github.frtu.kotlin.patterns

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Dependency injection happen during construction where reference is injected to class.
 *
 * Registry of Element <E> that allow to choose which reference to use at runtime using name.
 *
 * @author Frédéric TU
 * @since 1.1.4
 * @param <K> Type of the Key
 * @param <E> Type of element registered
 */
abstract class AbstractRegistry<K, E>(
    /** Type name for Log purpose */
    private val type: String,
    /** Storage for Element */
    private val registry: MutableMap<K, E> = mutableMapOf(),
) {
    open fun getElement(name: K): E {
        val normalizedName = normalizedName(name)
        val result = registry[normalizedName] ?: throw UnrecognizedElementException(type, normalizedName.toString())
        logger.debug("Resolve $type:[$name] with normalizedName=[$normalizedName] and result=[${result}]")
        return result
    }

    /**
     * Internal way to add new element to the registry
     */
    protected open fun register(name: K, element: E): AbstractRegistry<K, E> {
        val normalizedName = normalizedName(name)
        logger.debug("register $type using normalizedName=$normalizedName")
        registry.putIfAbsent(normalizedName, element)
        return this
    }

    /**
     * Allow override name normalization using any convention
     * @param name on which normalization can apply
     */
    protected open fun normalizedName(name: K) = name

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
}