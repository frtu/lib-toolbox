package com.github.frtu.workflow.serverlessworkflow

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.workflow.serverlessworkflow.state.case
import com.github.frtu.workflow.serverlessworkflow.state.sleep
import com.github.frtu.workflow.serverlessworkflow.state.switch
import com.github.frtu.workflow.serverlessworkflow.state.operation
import com.github.frtu.workflow.serverlessworkflow.state.call
import com.github.frtu.workflow.serverlessworkflow.state.using
import com.github.frtu.workflow.serverlessworkflow.trigger.TimeTriggerBuilder.Companion.UTC
import com.github.frtu.workflow.serverlessworkflow.trigger.byEvent
import com.github.frtu.workflow.serverlessworkflow.trigger.byTime
import io.kotlintest.matchers.string.contain
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.junit5.MockKExtension
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
        val sleepStateName = "SleepState"

        val triggerName = "EventTriggerName"
        val eventType = "validation.init"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = workflow(workflowName) {
            triggered {
                +byEvent(eventType, name = triggerName) {
                    transition = sleepStateName
                }
            }
            states {
                +sleep(duration = "PT1M", name = sleepStateName) {
                    this.terminate = true
                }
            }
        }
        logger.debug("result:${jsonPrettyPrint(result)}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe workflowName
    }

    @Test
    fun `Call workflow DSL with time trigger`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val workflowName = "Workflow_${UUID.randomUUID()}"

        val triggerName = "TimeTriggerName"
        val sleepStateName = "SleepState"

        val expression = "0 15,30,45 * ? * *"
        val validUntil = "2021-11-05T08:15:30-05:00"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = workflow(workflowName) {
            triggered {
                +byTime(triggerName) {
                    this.expression = expression
                    this.validUntil = validUntil
                    this.transition = sleepStateName
                }
            }
            states {
                +sleep(duration = "PT1M", name = sleepStateName) {
                    this.terminate = true
                }
            }
        }
        logger.debug("result:${jsonPrettyPrint(result)}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result?.start?.schedule) {
            shouldNotBeNull()
            this.cron?.expression shouldBe expression
            this.cron?.validUntil shouldBe validUntil
            this.timezone shouldBe UTC
        }
    }

    @Test
    fun `Call workflow DSL without start state`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val workflowName = "Workflow_${UUID.randomUUID()}"
        val sleepStateName = "SleepState"

        //--------------------------------------
        // 2. Execute & Validate
        //--------------------------------------
        val exception = shouldThrow<IllegalArgumentException> {
            workflow(workflowName) {
                states {
                    +sleep(duration = "PT1M", name = sleepStateName) {
                        this.terminate = true
                    }
                }
            }
        }
        exception.message should contain("triggered ")
    }

    @Test
    fun `Test valid workflow`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val workflowName = "Workflow_${UUID.randomUUID()}"

        val triggerName = "EventTriggerName"
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
                    transition = switchStateName
                }
            }
            states {
                +switch(switchStateName) {
                    +case("\${ #event.type = 'validation.init' }", name = "validation.init") {
                        transition = sleepStateName
                    }
                    +case("\${ #event.type = 'validation.approved' }", name = "validation.approved") {
                        transition = operationStateName
                    }
                    default(transition = sleepStateName)
                }
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
                    terminate = true
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
        result.start?.stateName shouldBe switchStateName
        with(result.states.filterIsInstance<EventState>()[0]) {
            with(this) {
                type shouldBe DefaultState.Type.EVENT
                name shouldBe triggerName
                transition?.nextState shouldBe switchStateName
                shouldBeInstanceOf<EventState> { eventState ->
                    eventState.onEvents.first().eventRefs?.first() shouldBe eventType
                }
            }
        }
        with(result.states.filterIsInstance<SleepState>()[0]) {
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

    @Test
    fun `Test invalid workflow but autofix`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val workflowName = "Workflow_${UUID.randomUUID()}"

        val triggerName = "EventTriggerName"
        val eventType = "validation.init"

        val sleepStateName = "Delay"
        val sleepDuration = "PT5S"

        val switchStateName = "ConditionEventType"

        val operationStateName = "OperationState"
        val parameterValueId = UUID.randomUUID().toString()
        val parameterValueName = "\${ variable.name }"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = workflow {
            name = workflowName
            triggered {
                +byEvent(eventType, name = triggerName) {
                    transition = operationStateName
                }
            }
            states {
                +operation(operationStateName) {
                    +(call(ServiceCall::query) using {
                        ServiceRequest::id with parameterValueId
                    })
                    +(call(ServiceCall::query) using {
                        ServiceRequest::id with parameterValueId
                        ServiceRequest::name with parameterValueName
                    })
                }
            }
        }
        logger.debug("result:${jsonPrettyPrint(result)}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result.states.last()) {
            shouldBeTypeOf<OperationState>()
            name shouldBe operationStateName
            end?.isTerminate shouldBe true
        }
    }

    internal fun jsonPrettyPrint(result: Any): String =
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)

    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(this::class.java)
}
