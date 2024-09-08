package com.github.frtu.kotlin.flow.errors

import org.slf4j.Logger

/**
 * Basic ErrorHandler to log errors
 * @author Frédéric TU
 * @since 1.1.4
 */
class LogErrorHandler(private val logger: Logger) : ErrorHandler {
    override fun warn(throwable: Throwable) {
        logger.warn(throwable.message, throwable)
    }

    override fun error(throwable: Throwable) {
        logger.error(throwable.message, throwable)
    }
}
