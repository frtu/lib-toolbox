package com.github.frtu.kotlin.ai.os.llm.agent.durable.temporal.activity

import com.fasterxml.jackson.databind.JsonNode
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

/**
 * Encapsulate a Tool as an Activity
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
@ActivityInterface
interface ToolAsActivity {
    @ActivityMethod
    fun doExecute(parameter: JsonNode): JsonNode
}