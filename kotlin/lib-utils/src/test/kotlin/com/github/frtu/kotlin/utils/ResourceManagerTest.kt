package com.github.frtu.kotlin.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class ResourceManagerTest {
    data class DummyBean(val name: String, val value: String)

    @Test
    fun createFromFile() {
        val dummyBean = ResourceManager().createFromFile(DummyBean::class.java, "classpath:dummy-bean.json")!!
        logger.debug("Found bean:${dummyBean}")
        assertThat(dummyBean.name).isEqualTo("bean_name")
        assertThat(dummyBean.value).isEqualTo("bean_value")
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}