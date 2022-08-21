package com.github.frtu.sample.serverless.workflow

import io.serverlessworkflow.api.validation.ValidationError

class ServerlessValidationException(val errors: List<ValidationError>) : RuntimeException()