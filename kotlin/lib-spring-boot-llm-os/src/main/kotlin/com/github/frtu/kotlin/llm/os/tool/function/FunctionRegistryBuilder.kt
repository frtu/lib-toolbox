package com.github.frtu.kotlin.llm.os.tool.function

import com.github.frtu.kotlin.llm.os.builder.BuilderMarker

class FunctionRegistryBuilder(
    private val functionRegistry: FunctionRegistry = FunctionRegistry()
) {
    fun register(function: Function<*, *>) = functionRegistry.registerFunction(function)

    fun build(): FunctionRegistry = functionRegistry
}


@BuilderMarker
fun registry(actions: FunctionRegistryBuilder.() -> Unit): FunctionRegistry =
    FunctionRegistryBuilder().apply(actions).build()
