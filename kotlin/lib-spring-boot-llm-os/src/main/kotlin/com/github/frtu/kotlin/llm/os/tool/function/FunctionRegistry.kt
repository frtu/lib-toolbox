package com.github.frtu.kotlin.llm.os.tool.function

import com.aallam.openai.api.chat.ChatCompletionFunction
import com.github.frtu.kotlin.llm.os.llm.openai.toChatCompletionFunction
import org.slf4j.LoggerFactory

/**
 * Registry for all usable functions
 */
class FunctionRegistry(
    private val registry: MutableList<Function<*, *>> = mutableListOf(),
) {
    fun getRegistry(): List<ChatCompletionFunction> = registry.map { it.toChatCompletionFunction() }

    fun getFunction(name: String): Function<*, *> = registry.first { name == it.name }
        ?: error("Function $name not found")

    fun registerFunction(function: Function<*, *>) {
        logger.debug(
            "Registering new function: name=[${function.name}] description=[${function.description}] " +
                    " parameterJsonSchema=[${function.parameterJsonSchema}]"
        )
        registry.add(function)
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}