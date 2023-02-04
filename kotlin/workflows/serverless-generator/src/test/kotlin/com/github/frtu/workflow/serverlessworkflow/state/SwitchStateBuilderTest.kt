package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.kotlin.utils.io.toJsonString
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SwitchState
import io.serverlessworkflow.api.switchconditions.DataCondition
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("SwitchStateBuilder tests")
@ExtendWith(MockKExtension::class)
internal class SwitchStateBuilderTest {
    @Test
    fun `Call short builder for Case DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val dataConditionName = "condition name"
        val condition = "\${ #event.type = 'validation.approved' }"
        val transition = "NextState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = case(name = dataConditionName, condition = condition) {
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe dataConditionName
        result.shouldBeInstanceOf<DataCondition>()
        result.condition shouldBe condition
        result.transition?.nextState shouldBe transition
    }

    @Test
    fun `Call bracket builder for Case DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val dataConditionName = "condition name"
        val condition = "\${ #event.type = 'validation.approved' }"
        val transition = "NextState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = case(condition) {
            this.name = dataConditionName
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe dataConditionName
        result.shouldBeInstanceOf<DataCondition>()
        result.condition shouldBe condition
        result.transition?.nextState shouldBe transition
    }

    @Test
    fun `Call bracket builder for Case DSL with termination`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val dataConditionName = "condition name"
        val condition = "\${ #event.type = 'validation.approved' }"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = case(condition) {
            this.name = dataConditionName
            this.terminate = true
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe dataConditionName
        result.shouldBeInstanceOf<DataCondition>()
        result.condition shouldBe condition
        result.transition.shouldBeNull()
        result.end.isTerminate shouldBe true
    }

    @Test
    fun `Call short builder for Switch State DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val stateName = "Switch state name"
        val conditions = listOf(
            "validation.init" to "ValidationInitialized",
            "validation.approved" to "ValidationApproved",
        )
        val defaultTransition = "DefaultState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = switch(stateName) {
            +case("\${ #event.type = '${conditions[0].first}' }", name = conditions[0].first) {
                this.transition = conditions[0].second
            }
            +case("\${ #event.type = '${conditions[1].first}' }", name = conditions[1].first) {
                this.transition = conditions[1].second
            }
            default(transition = defaultTransition)
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe stateName
        result.shouldBeInstanceOf<SwitchState>()
        result.type shouldBe DefaultState.Type.SWITCH
        result.dataConditions.size shouldBe conditions.size
        (0..1).forEach { index ->
            with(result.dataConditions[index]) {
                name shouldBe conditions[index].first
                condition shouldContain conditions[index].first
                transition?.nextState shouldBe conditions[index].second
            }
        }
        result.defaultCondition.transition?.nextState shouldBe defaultTransition
    }

    @Test
    fun `Call short builder for Switch State DSL with termination`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val stateName = "Switch state name"
        val conditions = listOf(
            "validation.init" to "ValidationInitialized",
            "validation.approved" to "ValidationApproved",
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = switch(stateName) {
            +case("\${ #event.type = '${conditions[0].first}' }", name = conditions[0].first) {
                this.transition = conditions[0].second
            }
            +case("\${ #event.type = '${conditions[1].first}' }", name = conditions[1].first) {
                this.transition = conditions[1].second
            }
            default(terminate = true)
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.transition.shouldBeNull()
        result.defaultCondition.end?.isTerminate shouldBe true
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
