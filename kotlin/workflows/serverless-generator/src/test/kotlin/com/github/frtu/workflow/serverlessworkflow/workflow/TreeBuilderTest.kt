package com.github.frtu.workflow.serverlessworkflow.workflow

import com.github.frtu.workflow.serverlessworkflow.state.*
import com.github.frtu.workflow.serverlessworkflow.trigger.byEvent
import com.github.frtu.workflow.serverlessworkflow.workflow
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldNot
import io.kotlintest.shouldNotBe
import io.serverlessworkflow.api.end.End
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.OperationState
import io.serverlessworkflow.api.states.SleepState
import io.serverlessworkflow.api.states.SwitchState
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import sample.ServiceCall
import sample.ServiceRequest
import java.util.*

class TreeBuilderTest {
    @Test
    fun `test buildTree with auto terminate node`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val sleepStateName = "Delay"
        val sleepDuration = "PT5S"

        val switchStateName = "ConditionEventType"

        val operationStateName = "OperationState"
        val parameterValueId = UUID.randomUUID().toString()
        val parameterValueName = "\${ variable.name }"

        val states = listOf(
            switch(switchStateName) {
                +case("\${ #event.type = 'validation.init' }", name = "validation.init") {
                    transition = sleepStateName
                }
                +case("\${ #event.type = 'validation.approved' }", name = "validation.approved") {
                    transition = operationStateName
                }
                default(transition = sleepStateName)
            },
            sleep(sleepStateName) {
                duration = sleepDuration
                transition = operationStateName
            },
            operation(operationStateName) {
                +(call(ServiceCall::query) using {
                    ServiceRequest::id with parameterValueId
                })
                +(call(ServiceCall::query) using {
                    ServiceRequest::id with parameterValueId
                    ServiceRequest::name with parameterValueName
                })
            }
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val treeBuilder = TreeBuilder()
        val result = treeBuilder.buildTree(switchStateName, states)
        logger.debug("result:$result}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            value.name shouldBe switchStateName
            value.shouldBeTypeOf<SwitchState>()
            value.end?.isTerminate shouldNotBe true
            children.shouldNotBeEmpty()
        }
        with(result.children[0]) {
            value.name shouldBe sleepStateName
            value.shouldBeTypeOf<SleepState>()
            value.end?.isTerminate shouldNotBe true
            children.shouldNotBeEmpty()
            with(children[0]) {
                value.name shouldBe operationStateName
                value.shouldBeTypeOf<OperationState>()
                value.end?.isTerminate shouldBe true
                children.shouldBeEmpty()
            }
        }
        with(result.children[1]) {
            value.name shouldBe sleepStateName
            value.shouldBeTypeOf<SleepState>()
            value.end?.isTerminate shouldNotBe true
            children.shouldNotBeEmpty()
            with(children[0]) {
                value.name shouldBe operationStateName
                value.shouldBeTypeOf<OperationState>()
                value.end?.isTerminate shouldBe true
                children.shouldBeEmpty()
            }
        }
        with(result.children[2]) {
            value.name shouldBe operationStateName
            value.shouldBeTypeOf<OperationState>()
            value.end?.isTerminate shouldBe true
            children.shouldBeEmpty()
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}