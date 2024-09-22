package com.github.frtu.kotlin.llm.os.tool

import com.aallam.openai.api.chat.ChatCompletionFunction
import kotlin.reflect.KFunction2
import org.slf4j.LoggerFactory

/**
 * Registry for all usable functions
 */
class FunctionRegistry(
    private val registry: MutableList<Function> = mutableListOf(),
) {
    fun getRegistry(): List<ChatCompletionFunction> = registry.map { it.toChatCompletionFunction() }
    fun getAvailableFunctions(): Map<String, KFunction2<String, String, String>> = registry.associate { it.name to it.action }

    fun getFunction(name: String): Function = registry.first { name == it.name }
        ?: error("Function $name not found")

    fun registerFunction(
        name: String,
        description: String,
        kFunction2: KFunction2<String, String, String>,
        parameterClass: Class<*>,
        returnClass: Class<*>,
    ) = registerFunction(function(name, description, kFunction2, parameterClass, returnClass))

    fun registerFunction(
        name: String,
        description: String,
        kFunction2: KFunction2<String, String, String>,
        parameterJsonSchema: String,
        returnJsonSchema: String,
    ) = registerFunction(function(name, description, kFunction2, parameterJsonSchema, returnJsonSchema))

    fun registerFunction(function: Function) {
        logger.debug(
            "Registering new function: name=[${function.name}] description=[${function.description}] " +
                    "function:[${function.action.name}] parameterJsonSchema=[${function.parameterJsonSchema}]"
        )
        registry.add(function)
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}