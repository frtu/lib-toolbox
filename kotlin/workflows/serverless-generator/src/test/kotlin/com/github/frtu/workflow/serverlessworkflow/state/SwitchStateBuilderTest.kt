package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.kotlin.utils.io.toJsonString
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
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
        val condition = "\${ #event.type = 'account.created' }"
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
        result.condition shouldBe condition
        result.transition?.nextState shouldBe transition
    }

    @Test
    fun `Call bracket builder for Case DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val dataConditionName = "condition name"
        val condition = "\${ #event.type = 'account.created' }"
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
        result.condition shouldBe condition
        result.transition?.nextState shouldBe transition
    }

    @Test
    fun `Call short builder for Switch State DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val stateName = "Switch state name"
        val conditions = listOf(
            "account.created" to "AccountCreatedState",
            "account.activated" to "AccountActivatedState",
        )
        val defaultTransition = "DefaultState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = switch(stateName) {
            +case("\${ #event.type = 'account.created' }", name = conditions[0].first) {
                this.transition = conditions[0].second
            }
            +case("\${ #event.type = 'account.activated' }", name = conditions[1].first) {
                this.transition = conditions[1].second
            }
            default(transition = defaultTransition)
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe stateName
        result.dataConditions.size shouldBe 2
        (0..1).forEach { index ->
            with(result.dataConditions[index]) {
                name shouldBe conditions[index].first
                condition shouldContain conditions[index].first
                transition?.nextState shouldBe conditions[index].second
            }
        }
        result.defaultCondition.transition?.nextState shouldBe defaultTransition
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
