package com.github.frtu.kotlin.flow.errors

/**
 * Handler for Errors and Exceptions
 * @author Frédéric TU
 * @since 1.1.4
 */
interface ErrorHandler {
    /**
     * @param throwable details about the occurred error
     */
    fun warn(throwable: Throwable)

    /**
     * @param throwable details about the occurred error
     */
    fun error(throwable: Throwable)
}
