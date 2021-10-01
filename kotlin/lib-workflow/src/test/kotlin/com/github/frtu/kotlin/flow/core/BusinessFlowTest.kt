package com.github.frtu.kotlin.flow.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

internal class BusinessFlowTest {
    @Test
    fun `Check flowName`() {
        val flowName = "Name1"
        val businessFlow = BusinessFlow(flowName)
        assertThat(businessFlow.flowName).isEqualTo(flowName)
    }

    @Test
    fun `Test doValidation`() {
        val businessFlow = BusinessFlow("Name1")
        val result = businessFlow.execute(
            Event(
                id = UUID.randomUUID(),
                eventTimeEpochMilli = Instant.now().toEpochMilli(),
                name = "EventName",
                description = "Event description",
            )
        )
        assertThat(result).isEqualTo("SUCCESS")
    }
}