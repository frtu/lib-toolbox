package com.github.frtu.kotlin.patterns

/**
 * An AbstractRegistry that uses String as a key
 *
 * @author Frédéric TU
 * @since 1.1.4
 * @param <E> Type of element registered
 */
abstract class AbstractRegistryStringKeys<E>(
    /** Type name for Log purpose */
    type: String,
    /** Storage for Element */
    registry: MutableMap<String, E> = mutableMapOf(),
    /** Registry namePrefix - Use it when creating many registries from one shared resource */
    private val namePrefix: String = "",
) : AbstractRegistry<String, E>(type, registry) {

    /**
     * Allow override name normalization using any convention
     * @param name on which normalization can apply
     */
    override fun normalizedName(name: String) = "$namePrefix$name"
}
