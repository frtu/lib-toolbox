package com.github.frtu.kotlin.flow.tool

import com.github.frtu.kotlin.flow.model.Event.Companion.event
import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ToolFlowTest {
    @Test
    fun `Test execute`() {
        runBlocking {
            val parameter = event().toJsonString().toJsonNode()
            val businessFlow = SampleToolFlow()
            val result = businessFlow.execute(parameter)
            result.shouldNotBeNull()
            result.textValue() shouldBe "SUCCESS"
        }
    }

    @Test
    fun `Test doValidation negative case`() {
        shouldThrow<IllegalArgumentException> {
            SampleToolFlow().execute(event(null))
        }
    }

    @Test
    fun `Test execute - Parameter exception`() {
        shouldThrow<IllegalStateException> {
            SampleToolFlow(shouldSucceed = false)
                .execute(event())
        }
    }
}
