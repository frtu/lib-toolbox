package com.github.frtu.kotlin.flow.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import java.time.Instant
import java.util.*

internal class SampleBusinessFlowTest {
    @Test
    fun `Check flowName`() {
        val flowName = "Name1"
        val businessFlow = SampleBusinessFlow(flowName)
        assertThat(businessFlow.flowName).isEqualTo(flowName)
    }

    @Test
    fun `Test doValidation`() {
        val businessFlow = SampleBusinessFlow("RegularFlow")
        val result = businessFlow.execute(event())
        assertThat(result).isEqualTo("SUCCESS")
    }

    @Test
    fun `Test doValidation negative case`() {
        assertThrows<IllegalArgumentException> {
            SampleBusinessFlow("NullDescription").execute(event(null))
        }
    }

    @Test
    fun `Test execute - Parameter exception`() {
        assertThrows<IllegalStateException> {
            SampleBusinessFlow("RegularFlowWithIssue", false)
                .execute(event())
        }
    }

    private fun event(description: String? = "Event description") = Event(
        id = UUID.randomUUID(),
        eventTimeEpochMilli = Instant.now().toEpochMilli(),
        name = "EventName",
        description = description,
    )
}