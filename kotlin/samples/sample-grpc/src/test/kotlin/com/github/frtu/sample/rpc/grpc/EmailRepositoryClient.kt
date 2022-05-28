package com.github.frtu.sample.rpc.grpc

import com.github.frtu.sample.grpc.EmailHistoryItem
import com.github.frtu.sample.grpc.EmailRepositoryServiceGrpcKt
import com.github.frtu.sample.grpc.byId
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.Closeable
import java.util.*
import java.util.concurrent.TimeUnit

class EmailRepositoryClient(private val channel: ManagedChannel) : Closeable {
    private val stub: EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineStub =
        EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineStub(channel)

    suspend fun queryOneById(id: String): EmailHistoryItem = stub.queryOne(byId { this.id = id })

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

/**
 * Greeter, uses first argument as name to greet if present;
 * greets "world" otherwise.
 */
suspend fun main(args: Array<String>) {
    val port = 9090

    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
    val client = EmailRepositoryClient(channel)

    val argId = args.singleOrNull() ?: "f603d6f8-fa77-4f0b-a2bd-1b562e9ba67a"

    val emailHistoryItem = client.queryOneById(argId)
    println("Received: $emailHistoryItem")
}
