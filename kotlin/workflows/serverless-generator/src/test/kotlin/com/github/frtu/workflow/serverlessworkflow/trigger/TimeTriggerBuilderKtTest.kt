package com.github.frtu.workflow.serverlessworkflow.trigger

import com.github.frtu.kotlin.utils.io.toJsonString
import io.kotlintest.matchers.numerics.shouldBeGreaterThanOrEqual
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import io.serverlessworkflow.api.schedule.Schedule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("TimeTriggerBuilder tests")
@ExtendWith(MockKExtension::class)
internal class TimeTriggerBuilderKtTest {
    @Test
    fun `Call short builder for Cron DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val expression = "0 15,30,45 * ? * *"
        val validUntil = "2021-11-05T08:15:30-05:00"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = cron(expression = expression, validUntil = validUntil)
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.expression shouldBe expression
        result.validUntil shouldBe validUntil
    }

    @Test
    fun `Call bracket builder for Cron DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val expression = "0 15,30,45 * ? * *"
        var validUntil = "2021-11-05T08:15:30-05:00"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = cron(expression = expression) {
            this.validUntil = validUntil
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.expression shouldBe expression
        result.validUntil shouldBe validUntil
    }

    @Test
    fun `Call short builder for byEvent DSL`() {
        //--------------------------------------
        // 1. Init vars
        //--------------------------------------
        val triggerName = "trigger name"
        val transition = "NextState"

        val expression = "0 15,30,45 * ? * *"
        var validUntil = "2021-11-05T08:15:30-05:00"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = byTime(name = triggerName) {
            this.expression = expression
            this.validUntil = validUntil
            this.transition = transition
        }
        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.name shouldBe triggerName
        result.transition shouldBe transition

        with(result.toResult()) {
            size shouldBeGreaterThanOrEqual 1
            with(this[0]?.schedule) {
                this.shouldBeInstanceOf<Schedule> { schedule ->
                    schedule.cron.shouldNotBeNull()
                    schedule.cron.expression shouldBe expression
                    schedule.cron.validUntil shouldBe validUntil
                }
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}