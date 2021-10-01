package com.github.frtu.kotlin.flow.errors

import org.slf4j.Logger

/**
 * Basic ErrorHandler to log errors
 * @author frtu
 * @since 1.1.4
 */
class LogErrorHandler(private val logger: Logger) : ErrorHandler {
    override fun handle(throwable: Throwable) {
        logger.error(throwable.message, throwable)
    }
}
