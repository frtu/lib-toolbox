package com.github.frtu.kotlin.tool.transition

import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.execution.TypedAction
import com.github.frtu.kotlin.action.management.ActionMetadata
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNot
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import sample.tool.IdentityTool

class HandoffToToolTest {
    @Test
    fun getNextAction(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val nextAction = IdentityTool()
        val handoffToTool = HandoffToTool(nextAction)

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = handoffToTool.execute(Unit)
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            // Redirection should check if result is an Action
            this should beInstanceOf<GenericAction>()
            this shouldNot beInstanceOf<TypedAction<*, *>>()

            // OR check if instance of ActionMetadata to redirect
            this should beInstanceOf<ActionMetadata>()
            // Redirect to the next action id
            id shouldBe nextAction.id
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}