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

    override operator fun get(name: String): E = try {
        super.get(name)
    } catch (e: UnrecognizedElementException) {
        super.get(DEFAULT_KEY)
    }

    companion object {
        const val DEFAULT_KEY = "default"
    }
}
