package com.github.frtu.sample.exception

/** Exception triggered by inconsistent business logic <=> HTTP status 4xx */
open class BusinessException(message: String? = null, cause: Throwable? = null) :
    IllegalArgumentException(message, cause)

/** Parameter invalid */
open class InvalidParameterException(message: String? = null, cause: Throwable? = null) :
    BusinessException(message, cause)

/** When requested id cannot be found in store <=> HTTP status 404 */
class DataNotFound(id: String, cause: Throwable? = null) : InvalidParameterException("Cannot find ID:$id", cause)