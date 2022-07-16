package com.github.frtu.sample.config

import com.github.frtu.kotlin.grpc.instrumentation.server.ServerLogGrpcInterceptor
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor

@GrpcGlobalServerInterceptor
class GrpcServerLogGrpcInterceptor: ServerLogGrpcInterceptor()
