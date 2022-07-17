package com.github.frtu.sample.rpc.grpc

import com.github.frtu.sample.exception.InvalidParameterException
import io.grpc.Status
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler

@GrpcAdvice
class GrpcExceptionAdvice {
    @GrpcExceptionHandler
    fun handleInvalidArgument(e: InvalidParameterException): Status =
        Status.INVALID_ARGUMENT.withDescription(e.message).withCause(e)

    @GrpcExceptionHandler
    fun handleInvalidArgument(e: IllegalArgumentException): Status =
        Status.INVALID_ARGUMENT.withDescription(e.message).withCause(e)
}
