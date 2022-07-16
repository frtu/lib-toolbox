package com.github.frtu.kotlin.grpc.instrumentation

enum class GrpcEventEnum(var label: String) {
    CLIENT_REQUEST("client_request"),
    CLIENT_RESPONSE("client_response"),
    SERVER_REQUEST("server_request"),
    SERVER_RESPONSE("server_response");
}
