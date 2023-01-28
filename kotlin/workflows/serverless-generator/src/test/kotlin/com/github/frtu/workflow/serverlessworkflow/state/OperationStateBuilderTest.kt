package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.kotlin.utils.io.toJsonString
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
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
    fun `Call short builder for Action DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val actionName = "action name"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = action(actionName) {
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe actionName
    }

    @Test
    fun `Call bracket builder for Action DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val actionName = "action name"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = action(actionName) {
            call(ServiceCall::query) using arrayOf(
                ServiceRequest::id.name to UUID.randomUUID().toString()
            )
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe actionName
        with(result.functionRef) {
            refName shouldBe "query"
            arguments.size() shouldBe 1
            println(arguments[0])
            with(arguments[0]) {
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
            "action1" to "AccountCreatedState",
            "action2" to "AccountActivatedState",
        )
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = operation(stateName) {
            +action(name = actions[0].first) {
            }
            +action(name = actions[1].first) {
            }
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
            }
        }
        result.transition?.nextState shouldBe transition
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}

