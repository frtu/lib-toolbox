package com.github.frtu.sample.temporal.staticwkf

import com.github.frtu.kotlin.utils.io.toJsonString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class SubscriptionEventTest {
    @Test
    fun getData() {
        println(
            SubscriptionEvent(
                data = "",
                type = "subscription",
                id = UUID.randomUUID(),
                timeMillis = Instant.now().toEpochMilli(),
                metadata = emptyMap(),
            ).toJsonString()
        )
    }
}
