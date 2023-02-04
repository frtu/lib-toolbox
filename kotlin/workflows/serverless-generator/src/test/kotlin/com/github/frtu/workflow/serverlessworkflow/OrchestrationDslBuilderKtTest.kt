package com.github.frtu.workflow.serverlessworkflow

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.workflow.serverlessworkflow.state.case
import com.github.frtu.workflow.serverlessworkflow.state.sleep
import com.github.frtu.workflow.serverlessworkflow.state.switch
import com.github.frtu.workflow.serverlessworkflow.state.operation
import com.github.frtu.workflow.serverlessworkflow.state.call
import com.github.frtu.workflow.serverlessworkflow.state.using
import com.github.frtu.workflow.serverlessworkflow.trigger.byEvent
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.OperationState
import io.serverlessworkflow.api.states.SleepState
import io.serverlessworkflow.api.states.SwitchState
import io.serverlessworkflow.api.states.EventState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import sample.ServiceCall
import sample.ServiceRequest
import java.util.UUID

@DisplayName("OrchestrationDslBuilder tests")
@ExtendWith(MockKExtension::class)
internal class OrchestrationDslBuilderKtTest {
    @Test
    fun `Call short builder for workflow DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val workflowName = "Workflow_${UUID.randomUUID()}"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = workflow(workflowName) {
            states {
                +sleep(duration = "PT1M") { }
            }
        }
        logger.debug("result:${jsonPrettyPrint(result)}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe workflowName
    }

    @Test
    fun `Test valid workflow`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val workflowName = "Workflow_${UUID.randomUUID()}"

        val triggerName = "TriggerName"
        val eventType = "validation.init"

        val sleepStateName = "Delay"
        val sleepDuration = "PT5S"

        val switchStateName = "ConditionEventType"

        val operationStateName = "OperationState"
        val parameterValueId = UUID.randomUUID().toString()
        val parameterValueName = "\${ variable.name }"

        val externalBuiltStates = listOf(sleep("${sleepStateName}1") {
            duration = sleepDuration
            transition = operationStateName
        })

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = workflow {
            name = workflowName
            triggered {
                +byEvent(eventType, name = triggerName) {
                    transition = sleepStateName
                }
            }
            states {
                +sleep(sleepStateName) {
                    duration = sleepDuration
                    transition = operationStateName
                }
                +operation(operationStateName) {
                    +(call(ServiceCall::query) using {
                        ServiceRequest::id with parameterValueId
                    })
                    +(call(ServiceCall::query) using {
                        ServiceRequest::id with parameterValueId
                        ServiceRequest::name with parameterValueName
                    })
                    transition = switchStateName
                }
                +switch(switchStateName) {
                    +case("\${ #event.type = 'validation.init' }", name = "validation.init") {
                        transition = "ValidationInitialized"
                    }
                    +case("\${ #event.type = 'validation.approved' }", name = "validation.approved") {
                        transition = "ValidationApproved"
                    }
                    default(transition = "DefaultState")
                }
                append(externalBuiltStates)
            }
        }
        logger.debug("result:${jsonPrettyPrint(result)}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe workflowName
        result.states.size shouldBe 5
        result.start?.stateName shouldBe triggerName
        with(result.states.filterIsInstance<EventState>()[0]) {
            with(this) {
                type shouldBe DefaultState.Type.EVENT
                name shouldBe triggerName
                transition?.nextState shouldBe sleepStateName
                shouldBeInstanceOf<EventState> { eventState ->
                    eventState.onEvents.first().eventRefs?.first() shouldBe eventType
                }
            }
        }
        with(result.states.filterIsInstance<SleepState>()[1]) {
            with(this) {
                type shouldBe DefaultState.Type.SLEEP
                name shouldBe sleepStateName
                transition?.nextState shouldBe operationStateName
                shouldBeInstanceOf<SleepState> { sleepState ->
                    sleepState.duration shouldBe sleepDuration
                }
            }
        }
        with(result.states.filterIsInstance<OperationState>()[0]) {
            with(this) {
                type shouldBe DefaultState.Type.OPERATION
                name shouldBe operationStateName
                shouldBeInstanceOf<OperationState> { operationState ->
                    operationState.actions.size shouldBe 2
                    with(operationState.actions[0].functionRef) {
                        refName shouldBe ServiceCall::query.name
                        arguments.size() shouldBe 1
                        with(arguments.get(ServiceRequest::id.name)) {
                            isValueNode shouldBe true
                            asText() shouldBe parameterValueId
                        }
                    }
                    with(operationState.actions[1].functionRef) {
                        refName shouldBe ServiceCall::query.name
                        arguments.size() shouldBe 2
                        with(arguments.get(ServiceRequest::id.name)) {
                            isValueNode shouldBe true
                            asText() shouldBe parameterValueId
                        }
                        with(arguments.get(ServiceRequest::name.name)) {
                            isValueNode shouldBe true
                            asText() shouldBe parameterValueName
                        }
                    }
                }
            }
        }
        with(result.states.filterIsInstance<SwitchState>()[0]) {
            with(this) {
                type shouldBe DefaultState.Type.SWITCH
                name shouldBe switchStateName
                shouldBeInstanceOf<SwitchState> { switchState ->
                    switchState.dataConditions.size shouldBe 2
                }
            }
        }
    }

    private fun jsonPrettyPrint(result: Any): String =
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)

    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(this::class.java)
}
