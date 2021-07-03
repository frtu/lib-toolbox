package com.github.frtu.kotlin.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class BeanHelperTest {
    private val beanManager = BeanHelper()

    @Test
    fun `Testing beanManager MAP toStringOrNull if type is String`() {
        val map = mapOf<String, Any>("name" to "bean_name", "age" to 18)
        val toStringOrNull = beanManager.toStringOrNull(map, "name")
        logger.debug("toStringOrNull=$toStringOrNull")
        assertThat(toStringOrNull).isEqualTo("bean_name")
    }

    @Test
    fun `Testing beanManager MAP toStringOrNull if type is other`() {
        val map = mapOf<String, Any>("name" to "bean_name", "age" to 18)
        val toStringOrNull = beanManager.toStringOrNull(map, "age")
        logger.debug("toStringOrNull=$toStringOrNull")
        assertThat(toStringOrNull).isNull()
    }

    @Test
    fun `Testing beanManager LIST toStringOrNull if type is String`() {
        val list = listOf<Any>("bean_name", 18)
        val toStringOrNull = beanManager.toStringOrNull(list, 0)
        logger.debug("toStringOrNull=$toStringOrNull")
        assertThat(toStringOrNull).isEqualTo("bean_name")
    }

    @Test
    fun `Testing beanManager LIST toStringOrNull if type is other`() {
        val list = listOf<Any>("bean_name", 18)
        val toStringOrNull = beanManager.toStringOrNull(list, 1)
        logger.debug("toStringOrNull=$toStringOrNull")
        assertThat(toStringOrNull).isNull()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}