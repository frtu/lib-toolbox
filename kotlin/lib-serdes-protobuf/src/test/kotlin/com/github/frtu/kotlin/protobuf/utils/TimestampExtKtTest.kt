package com.github.frtu.kotlin.protobuf.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.Instant

internal class TimestampExtKtTest {
    @Test
    fun toTimestamp() {
        //--------------------------------------
        // 1. Constructor only call once
        //--------------------------------------
        val instant = Instant.now()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val timestampProto = instant.toTimestamp()
        val result = timestampProto.toEpochMilli()
        logger.debug("result=$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertThat(instant.toEpochMilli()).isEqualTo(result)
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}