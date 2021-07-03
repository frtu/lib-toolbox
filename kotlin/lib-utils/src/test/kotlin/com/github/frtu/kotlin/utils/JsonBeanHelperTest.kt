package com.github.frtu.kotlin.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class JsonBeanHelperTest {
    private val beanManager = JsonBeanHelper<Any>()

    @Test
    fun `Testing beanManager toBean`() {
        val dummyBean = beanManager.toBean(DummyBean::class.java, "classpath:dummy-bean.json")!!
        //--------------------------------------
        // Validate
        //--------------------------------------
        logger.debug("Found bean:${dummyBean}")
        assertThat(dummyBean.name).isEqualTo("bean_name")
        assertThat(dummyBean.value).isEqualTo("bean_value")
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}