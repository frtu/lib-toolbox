package com.github.frtu.sample.rpc.grpc

import com.github.frtu.kotlin.protobuf.utils.toTimestamp
import com.github.frtu.sample.grpc.EmailHistoryItem
import com.github.frtu.sample.grpc.EmailRepositoryServiceGrpcKt
import com.github.frtu.sample.grpc.email
import com.github.frtu.sample.grpc.emailHistoryItem
import com.github.frtu.sample.persistence.basic.STATUS
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.util.*
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
//    stub.queryOne(byId { this.id = "47601b76-9abd-4c81-ac45-f11bbff5c73b" }).apply {
//        println("Received: $this")
//    }

    // Query MANY
//    stub.queryMany(by { name = "test" }).collect {
//        println(it)
//        Thread.sleep(1000)
//    }

    // Insert MANY
    val insertFlow: Flow<EmailHistoryItem> = flow {
//        for (i in 1..5) {
        while (true) {
            delay(2000)
            emit(emailHistoryItem {
                this.id = UUID.randomUUID().toString()
                this.data = email {
                    this.receiver = "rndfred@gmail.com"
                    this.subject = "Mail subject"
                    this.content = "Lorem ipsum dolor sit amet."
                }
                this.creationTime = Instant.now().toTimestamp()
                this.updateTime = Instant.now().toTimestamp()
                this.status = STATUS.SENT.toString()
            })
        }
    }
    stub.insert(insertFlow)
        .collect {
            println(it)
//            Thread.sleep(1000)
        }

    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
}
