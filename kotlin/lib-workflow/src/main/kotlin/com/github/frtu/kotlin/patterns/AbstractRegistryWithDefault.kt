package com.github.frtu.kotlin.patterns

/**
 * An AbstractRegistry that returns a default value instead of throwing UnrecognizedElementException
 *
 * @author Frédéric TU
 * @since 1.1.4
 * @param <E> Type of element registered
 */
abstract class AbstractRegistryWithDefault<E>(
    defaultElement: E,
    /** Type name for Log purpose */
    type: String,
    /** Storage for Element */
    registry: MutableMap<String, E> = mutableMapOf(),
) : AbstractRegistry<String, E>(type, registry.apply { put(DEFAULT_KEY, defaultElement) }) {

    override fun getElement(name: String): E = try {
        super.getElement(name)
    } catch (e: UnrecognizedElementException) {
        super.getElement(DEFAULT_KEY)
    }

    companion object {
        const val DEFAULT_KEY = "default"
    }
}
