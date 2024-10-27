package com.github.frtu.kotlin.llm.os.tool.function

import com.aallam.openai.api.chat.ChatCompletionFunction
import com.github.frtu.kotlin.action.management.ActionRegistry
import com.github.frtu.kotlin.llm.os.llm.openai.toChatCompletionFunction
import org.slf4j.LoggerFactory

/**
 * Registry for all usable functions
 */
class FunctionRegistry(
    registry: MutableList<Function<*, *>> = mutableListOf(),
) : ActionRegistry<Function<*, *>>(registry) {
    fun getRegistry(): List<ChatCompletionFunction> = registry.values.map { it.toChatCompletionFunction() }
}