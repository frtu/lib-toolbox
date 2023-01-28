package com.github.frtu.workflow.serverlessworkflow

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.workflow.serverlessworkflow.state.case
import com.github.frtu.workflow.serverlessworkflow.state.sleep
import com.github.frtu.workflow.serverlessworkflow.state.switch
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SleepState
import io.serverlessworkflow.api.states.SwitchState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
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
        val workflowName = "Email_79dbd650-d901-409e-b148-78ae680fbd53"

        val sleepStateName = "DelayForUserToTakeAction"
        val sleepDuration = "PT5S"

        val switchStateName = "ConditionEventType"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = workflow {
            name = workflowName
            states {
                +sleep(name = sleepStateName) {
                    duration = sleepDuration
                }
                +switch(switchStateName) {
                    +case("\${ #event.type = 'account.created' }", name = "account.created") {
                        this.transition = "AccountCreatedState"
                    }
                    +case("\${ #event.type = 'account.activated' }", name = "account.activated") {
                        this.transition = "AccountActivatedState"
                    }
                    default(transition = "DefaultState")
                }
            }
        }
        logger.debug("result:${jsonPrettyPrint(result)}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe workflowName
        result.states.size shouldBe 2
        with(result.states[0]) {
            type shouldBe DefaultState.Type.SLEEP
            name shouldBe sleepStateName
            val sleepState = this as SleepState
            sleepState.duration shouldBe sleepDuration
        }
        with(result.states[1]) {
            type shouldBe DefaultState.Type.SWITCH
            name shouldBe switchStateName
            val switchState = this as SwitchState
            switchState.dataConditions.size shouldBe 2
        }
    }

    private fun jsonPrettyPrint(result: Any): String = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)

    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(this::class.java)
}
