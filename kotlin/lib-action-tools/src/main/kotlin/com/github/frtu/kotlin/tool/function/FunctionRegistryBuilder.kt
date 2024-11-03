package com.github.frtu.kotlin.tool.function

import com.github.frtu.kotlin.tool.builder.BuilderMarker

class FunctionRegistryBuilder(
    private val functionRegistry: FunctionRegistry = FunctionRegistry()
) {
    fun register(function: Function<*, *>) = functionRegistry.register(function)

    fun build(): FunctionRegistry = functionRegistry
}


@BuilderMarker
fun registry(actions: FunctionRegistryBuilder.() -> Unit): FunctionRegistry =
    FunctionRegistryBuilder().apply(actions).build()
