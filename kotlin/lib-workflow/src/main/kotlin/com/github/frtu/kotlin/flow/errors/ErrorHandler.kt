package com.github.frtu.kotlin.flow.errors

/**
 * Handler for Errors
 * @author frtu
 * @since 1.1.4
 */
interface ErrorHandler {
    /**
     * @param throwable details about the occurred error
     */
    fun handle(throwable: Throwable)
}
