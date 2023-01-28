package com.github.frtu.workflow.serverlessworkflow

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.workflow.serverlessworkflow.state.sleep
import io.kotlintest.matchers.numerics.shouldBeGreaterThanOrEqual
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SleepState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("OrchestrationDslBuilder tests")
@ExtendWith(MockKExtension::class)
internal class OrchestrationDslBuilderKtTest {
    @Test
    fun `Test valid workflow`() {
        //--------------------------------------
        // 1. Constructor only call once
        //--------------------------------------
        val sleepStateName = "DelayForUserToTakeAction"
        val sleepDuration = "PT5S"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = workflow {
            states {
                +sleep(stateName = sleepStateName) {
                    duration = sleepDuration
                }
            }
        }
        logger.debug("result:${jsonPrettyPrint(result)}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.states.size shouldBeGreaterThanOrEqual 1
        with(result.states[0]) {
            type shouldBe DefaultState.Type.SLEEP
            name shouldBe sleepStateName
            val sleepState = this as SleepState
            sleepState.duration shouldBe sleepDuration
        }
    }

    private fun jsonPrettyPrint(result: Any): String = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)

    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(this::class.java)
}
