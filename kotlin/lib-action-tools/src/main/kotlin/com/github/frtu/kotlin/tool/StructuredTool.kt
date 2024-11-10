package com.github.frtu.kotlin.tool

import com.github.frtu.kotlin.action.execution.TypedAction

/**
 * Structured `Tool` returning <INPUT, OUTPUT>
 *
 * @author Frédéric TU
 * @since 2.0.9
 */
interface StructuredTool<INPUT, OUTPUT> : Tool, TypedAction<INPUT, OUTPUT>
