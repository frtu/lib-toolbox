package com.github.frtu.kotlin.grpc.instrumentation.server

import com.github.frtu.kotlin.grpc.instrumentation.GrpcEventEnum
import com.github.frtu.kotlin.grpc.instrumentation.ProtoMethodParamLogger
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status

open class ServerLogGrpcInterceptor(
    private val protoMethodParamLogger: ProtoMethodParamLogger = ProtoMethodParamLogger(),
) : ServerInterceptor {

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>, headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val grpcServerCall: GrpcServerCall<ReqT, RespT> = GrpcServerCall(call, protoMethodParamLogger)
        val listener: ServerCall.Listener<ReqT> = next.startCall(grpcServerCall, headers)
        return ReqRespForwardingServerCallListener(call.methodDescriptor, listener, protoMethodParamLogger)
    }

    private class ReqRespForwardingServerCallListener<ReqT>(
        method: MethodDescriptor<*, *>,
        listener: ServerCall.Listener<ReqT>,
        private val protoMethodParamLogger: ProtoMethodParamLogger,
    ) : SimpleForwardingServerCallListener<ReqT>(listener) {
        var methodName: String = method.fullMethodName

        override fun onMessage(message: ReqT) {
            protoMethodParamLogger.log(GrpcEventEnum.SERVER_REQUEST, this.methodName, message)
            super.onMessage(message)
        }
    }

    private class GrpcServerCall<ReqT, RespT>(
        private var serverCall: ServerCall<ReqT, RespT>,
        private val protoMethodParamLogger: ProtoMethodParamLogger,
    ) : ServerCall<ReqT, RespT>() {
        override fun request(numMessages: Int) {
            serverCall.request(numMessages)
        }

        override fun sendHeaders(headers: Metadata) {
            serverCall.sendHeaders(headers)
        }

        override fun sendMessage(message: RespT) {
            val methodName = serverCall.methodDescriptor.fullMethodName
            protoMethodParamLogger.log(GrpcEventEnum.SERVER_RESPONSE, methodName, message)
            serverCall.sendMessage(message)
        }

        override fun close(status: Status, trailers: Metadata) {
            serverCall.close(status, trailers)
        }

        override fun isCancelled(): Boolean {
            return serverCall.isCancelled
        }

        override fun getMethodDescriptor(): MethodDescriptor<ReqT, RespT> {
            return serverCall.methodDescriptor
        }
    }
}