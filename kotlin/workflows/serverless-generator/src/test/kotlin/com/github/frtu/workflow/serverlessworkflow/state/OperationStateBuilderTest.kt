package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.kotlin.utils.io.toJsonString
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.actions.Action
import io.serverlessworkflow.api.states.DefaultState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import sample.ServiceCall
import sample.ServiceRequest
import java.util.*

@DisplayName("OperationStateBuilder tests")
@ExtendWith(MockKExtension::class)
internal class OperationStateBuilderTest {
    @Test
    fun `Call builder for Action DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val optionalActionName = "action name"
        val parameters = listOf(
            UUID.randomUUID().toString(),
            "\${ variable.name }",
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = call(ServiceCall::query, name = optionalActionName) using {
            ServiceRequest::id with parameters[0]
            ServiceRequest::name with parameters[1]
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe optionalActionName
        result.javaClass shouldBe Action::class.java
        with(result.functionRef) {
            refName shouldBe ServiceCall::query.name
            arguments.size() shouldBe parameters.size
            with(arguments.get(ServiceRequest::id.name)) {
                isValueNode shouldBe true
                asText() shouldBe parameters[0]
            }
            with(arguments.get(ServiceRequest::name.name)) {
                isValueNode shouldBe true
                asText() shouldBe parameters[1]
            }
        }
    }

    @Test
    fun `Call short builder for Operation State DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val stateName = "Operation state name"
        val transition = "DefaultState"
        val actions = listOf(
            "action1" to UUID.randomUUID().toString(),
            "action2" to "\${ variable.name }",
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = operation(stateName) {
            +(call(ServiceCall::query, name = actions[0].first) using {
                ServiceRequest::id with actions[0].second
            })
            +(call(ServiceCall::query, name = actions[1].first) using {
                ServiceRequest::id with actions[0].second
                ServiceRequest::name with actions[1].second
            })
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe stateName
        result.type shouldBe DefaultState.Type.OPERATION
        result.actions.size shouldBe actions.size
        (0..1).forEach { index ->
            with(result.actions[index]) {
                name shouldBe actions[index].first
                with(functionRef) {
                    refName shouldBe ServiceCall::query.name
                    with(arguments.get(ServiceRequest::id.name)) {
                        isValueNode shouldBe true
                        asText() shouldBe actions[0].second
                    }
                }
            }
        }
        with(result.actions[1].functionRef) {
            with(arguments.get(ServiceRequest::name.name)) {
                isValueNode shouldBe true
                asText() shouldBe actions[1].second
            }
        }
        result.transition?.nextState shouldBe transition
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
