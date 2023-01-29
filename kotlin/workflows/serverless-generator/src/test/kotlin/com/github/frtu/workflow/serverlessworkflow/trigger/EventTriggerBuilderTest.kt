package com.github.frtu.workflow.serverlessworkflow.trigger

import com.github.frtu.kotlin.utils.io.toJsonString
import com.github.frtu.workflow.serverlessworkflow.trigger.EventTriggerBuilder.Companion.EVENT_TRIGGER_DEFAULT_NAME
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.states.EventState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("EventTriggerBuilder tests")
@ExtendWith(MockKExtension::class)
internal class EventTriggerBuilderTest {

    @Test
    fun `Call short builder for byEvent DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val triggerName = "trigger name"
        val type = "validation.init"
        val transition = "NextState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = byEvent(type, name = triggerName) {
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result.toState()) {
            this?.name shouldBe triggerName
            this?.transition?.nextState shouldBe transition
            this.shouldBeInstanceOf<EventState> { eventState ->
                eventState.onEvents.first().eventRefs?.first() shouldBe type
            }
        }
    }

    @Test
    fun `byEvent DSL default name`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val type = "validation.init"
        val transition = "NextState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = byEvent(type) {
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result.toState()) {
            this?.name shouldBe EVENT_TRIGGER_DEFAULT_NAME
            this?.transition?.nextState shouldBe transition
            this.shouldBeInstanceOf<EventState> { eventState ->
                eventState.onEvents.first().eventRefs?.first() shouldBe type
            }
        }
    }

    @Test
    fun `Call bracket builder for byEvent DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val triggerName = "trigger name"
        val type = "validation.init"
        val transition = "NextState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = byEvent(type) {
            this.name = triggerName
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result.toState()) {
            this?.name shouldBe triggerName
            this?.transition?.nextState shouldBe transition
            this.shouldBeInstanceOf<EventState> { eventState ->
                eventState.onEvents.first().eventRefs?.first() shouldBe type
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
