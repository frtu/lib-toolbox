package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.kotlin.utils.io.toJsonString
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SleepState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("SleepStateBuilder tests")
@ExtendWith(MockKExtension::class)
internal class SleepStateBuilderTest {
    @Test
    fun `Call short builder for Sleep State DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val stateName = "state name"
        val duration = "PT5S"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = sleep(name = stateName, duration = duration)
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe stateName
        result.type shouldBe DefaultState.Type.SLEEP
        result.shouldBeInstanceOf<SleepState>() {
            it.duration shouldBe duration
        }
    }

    @Test
    fun `Call bracket builder for Sleep State DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val stateName = "state name"
        val duration = "PT5S"
        val transition = "DefaultState"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = sleep {
            this.name = stateName
            this.duration = duration
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe stateName
        result.shouldBeInstanceOf<SleepState>{
            it.duration shouldBe duration
        }
        result.transition?.nextState shouldBe transition
    }

    @Test
    fun `Call builder for Operation State DSL with termination`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val stateName = "state name"
        val duration = "PT5S"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = sleep {
            this.name = stateName
            this.duration = duration
            this.terminate = true
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe stateName
        result.transition.shouldBeNull()
        result.end.isTerminate shouldBe true
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
