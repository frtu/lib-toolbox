package com.github.frtu.kotlin.flow.core

import com.github.frtu.kotlin.flow.model.Event.Companion.event
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

internal class SampleFlowTest {
    @Test
    fun `Check flowName`() {
        val flowName = "Name1"
        val businessFlow = SampleFlow(flowName)
        businessFlow.flowName shouldBe flowName
    }

    @Test
    fun `Test doValidation`() {
        val businessFlow = SampleFlow("RegularFlow")
        val result = businessFlow.execute(event())
        result shouldBe "SUCCESS"
    }

    @Test
    fun `Test doValidation negative case`() {
        shouldThrow<IllegalArgumentException> {
            SampleFlow("NullDescription").execute(event(null))
        }
    }

    @Test
    fun `Test execute - Parameter exception`() {
        shouldThrow<IllegalStateException> {
            SampleFlow("RegularFlowWithIssue", false)
                .execute(event())
        }
    }
}