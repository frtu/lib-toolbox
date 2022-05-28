package com.github.frtu.sample.rpc.grpc

import com.github.frtu.sample.grpc.EmailRepositoryServiceGrpcKt
import com.github.frtu.sample.grpc.byId
import io.grpc.ManagedChannelBuilder
import java.util.concurrent.TimeUnit

/**
 * Greeter, uses first argument as name to greet if present;
 * greets "world" otherwise.
 */
suspend fun main() {
    val port = 9090
    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()

    val stub: EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineStub =
        EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineStub(channel)

    // Query ONE
    stub.queryOne(byId { this.id = "47601b76-9abd-4c81-ac45-f11bbff5c73b" }).apply {
        println("Received: $this")
    }

    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
}
