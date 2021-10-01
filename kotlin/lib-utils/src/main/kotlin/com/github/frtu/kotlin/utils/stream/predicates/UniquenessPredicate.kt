package com.github.frtu.kotlin.utils.stream.predicates

import java.util.function.Predicate

/**
 * Like DB column UNIQUE, allow to check a key unique and make sure it is only computed once
 *
 * NOTE : since it is usually stateful, make sure to manage his scope OR choose the right implementation
 * (by request OR for time window backed by a ephemeral store)
 *
 * @author Frédéric TU
 * @since 1.1.4
 */
interface UniquenessPredicate : Predicate<String> {
    /**
     * @return if this key is unique
     */
    fun isUnique(key: String): Boolean
}
