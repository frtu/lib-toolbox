package com.github.frtu.kotlin.utils.stream.predicates


/**
 * A STATEFUL UniquenessPredicate backed by a MutableCollection.
 *
 * NOTE : since it is stateful, make sure to discard it correctly (new instance per request or by holder object)
 *
 * @author Frédéric TU
 * @since 1.1.4
 */
class InMemoryUniquenessPredicate(
    private val uniqueKeysCollection: MutableCollection<String> = mutableListOf()
) : UniquenessPredicate {
    override fun isUnique(key: String): Boolean = test(key)

    override fun test(key: String): Boolean = if (!uniqueKeysCollection.contains(key)) {
        uniqueKeysCollection.add(key)
        true
    } else false
}
