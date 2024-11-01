package com.github.frtu.kotlin.action.execution

/**
 * An function that can be executed using specific class
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
interface TypedAction<INPUT, OUTPUT> : Action<INPUT, OUTPUT> {
    suspend fun execute(parameter: INPUT): OUTPUT
}