package com.github.frtu.kotlin.llm.os.agent.durable.temporal.activity

import com.fasterxml.jackson.databind.JsonNode
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

/**
 * Encapsulate a Tool as an Activity
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
@ActivityInterface
interface ToolAsActivity {
    @ActivityMethod
    fun doExecute(parameter: JsonNode): JsonNode
}