package com.github.frtu.sample.exception

/**
 * Exception triggered by System <=> HTTP status 5xx
 *
 * Should usually can trigger retry logic
 */
open class TechnicalException(message: String?, cause: Throwable?) : IllegalArgumentException(message, cause)
