package com.github.frtu.kotlin.utils.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class JsonBeanHelperTest {
    private val beanManager = JsonBeanHelper<Any>()

    @Test
    fun `Testing beanManager toBean`() {
        val dummyBean = beanManager.toBean("classpath:dummy-bean.json", DummyBean::class.java)!!
        //--------------------------------------
        // Validate
        //--------------------------------------
        logger.debug("Found bean:${dummyBean}")
        assertThat(dummyBean.name).isEqualTo("bean_name")
        assertThat(dummyBean.value).isEqualTo("bean_value")
    }

    @Test
    fun jsonToObj() {
        val jsonNode = """{"key": "value"}""".toJsonNode()
        assertThat(jsonNode["key"].textValue()).isEqualTo("value")
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}