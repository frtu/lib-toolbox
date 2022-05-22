package com.github.frtu.kotlin.protobuf.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ProtoUtilTest {
    @Test
    fun jsonToObj() {
        val jsonNode = """{"key": "value"}""".toJsonNode()
        assertThat(jsonNode["key"].textValue()).isEqualTo("value")
    }
}