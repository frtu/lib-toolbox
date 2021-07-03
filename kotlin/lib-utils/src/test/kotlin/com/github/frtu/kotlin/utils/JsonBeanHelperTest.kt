package com.github.frtu.kotlin.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.slf4j.LoggerFactory

internal class JsonBeanHelperTest {
    private val beanManager = JsonBeanHelper()

    @Test
    fun `Testing beanManager toBean`() {
        val dummyBean = beanManager.toBean(DummyBean::class.java, "classpath:dummy-bean.json")!!
        logger.debug("Found bean:${dummyBean}")
        assertThat(dummyBean.name).isEqualTo("bean_name")
        assertThat(dummyBean.value).isEqualTo("bean_value")
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}