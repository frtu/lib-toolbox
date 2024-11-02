package com.github.frtu.kotlin.action.tool.function

import com.github.frtu.kotlin.action.management.ActionRegistry

/**
 * Registry for all usable functions
 */
class FunctionRegistry(
    registry: MutableList<Function<*, *>> = mutableListOf(),
) : ActionRegistry<Function<*, *>>(registry)