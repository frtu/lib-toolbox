package com.github.frtu.kotlin.flow.core

import com.github.frtu.kotlin.flow.model.Event.Companion.event
import com.github.frtu.kotlin.flow.model.SampleFlow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException

internal class SampleFlowTest {
    @Test
    fun `Check flowName`() {
        val flowName = "Name1"
        val businessFlow = SampleFlow(flowName)
        assertThat(businessFlow.name).isEqualTo(flowName)
    }

    @Test
    fun `Test doValidation`() {
        val businessFlow = SampleFlow("RegularFlow")
        val result = businessFlow.execute(event())
        assertThat(result).isEqualTo("SUCCESS")
    }

    @Test
    fun `Test doValidation negative case`() {
        assertThrows<IllegalArgumentException> {
            SampleFlow("NullDescription").execute(event(null))
        }
    }

    @Test
    fun `Test execute - Parameter exception`() {
        assertThrows<IllegalStateException> {
            SampleFlow("RegularFlowWithIssue", false)
                .execute(event())
        }
    }
}