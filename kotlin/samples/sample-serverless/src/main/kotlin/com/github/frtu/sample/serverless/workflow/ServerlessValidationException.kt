package com.github.frtu.sample.serverless.workflow

import io.serverlessworkflow.api.validation.ValidationError

/**
 * An exception for InvalidConfiguration containing all the specific io.serverlessworkflow.api.validation.ValidationError
 */
class ServerlessValidationException(val errors: List<ValidationError>) :
    RuntimeException("Validation exception! Check 'errors' for more details.")