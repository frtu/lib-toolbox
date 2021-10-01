package com.github.frtu.kotlin.utils.stream.predicates

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UniquenessPredicateTest {

    @Test
    fun isUnique() {
        checkUniqueness(InMemoryUniquenessPredicate())
    }

    private fun checkUniqueness(uniquenessPredicate: UniquenessPredicate) {
        // key1
        Assertions.assertThat(uniquenessPredicate.isUnique("key1")).isTrue
        Assertions.assertThat(uniquenessPredicate.isUnique("key1")).isFalse
        // NEW key2
        Assertions.assertThat(uniquenessPredicate.isUnique("key2")).isTrue
        Assertions.assertThat(uniquenessPredicate.isUnique("key2")).isFalse
        // key1 should be consistent all along
        Assertions.assertThat(uniquenessPredicate.isUnique("key1")).isFalse
        Assertions.assertThat(uniquenessPredicate.isUnique("key2")).isFalse
    }
}