package com.github.frtu.sample.temporal.dynamicwkf.serverless

import io.serverlessworkflow.api.validation.ValidationError

class ServerlessValidationException(val errors: List<ValidationError>) : RuntimeException()
