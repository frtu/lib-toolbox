package com.github.frtu.kotlin.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class BeanManagerTest {
    data class DummyBean(val name: String, val value: String)

    private val beanManager = BeanManager()

    @Test
    fun `Testing beanManager toBean`() {
        val dummyBean = beanManager.toBean(DummyBean::class.java, "classpath:dummy-bean.json")!!
        logger.debug("Found bean:${dummyBean}")
        assertThat(dummyBean.name).isEqualTo("bean_name")
        assertThat(dummyBean.value).isEqualTo("bean_value")
    }

    @Test
    fun `Testing beanManager toStringOrNull if type is String`() {
        val map = mapOf<String, Any>("name" to "bean_name", "age" to 18)
        assertThat(beanManager.toStringOrNull(map,"name")).isEqualTo("bean_name")
    }

    @Test
    fun `Testing beanManager toStringOrNull if type is other`() {
        val map = mapOf<String, Any>("name" to "bean_name", "age" to 18)
        assertThat(beanManager.toStringOrNull(map, "age")).isNull()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}