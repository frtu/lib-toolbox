package com.github.frtu.sample

import com.github.frtu.kotlin.protobuf.utils.toTimestamp
import com.github.frtu.sample.grpc.*
import io.grpc.Server
import io.grpc.ServerBuilder
import java.time.Instant
import java.util.*

class EmailRepositoryServer(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(EmailRepositoryServiceHandler())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@EmailRepositoryServer.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class EmailRepositoryServiceHandler :
        EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineImplBase() {
        override suspend fun queryOne(request: ById): EmailHistoryItem = emailHistoryItem {
            this.id = UUID.randomUUID().toString()
            this.data = email {
                this.receiver = "rndfred@gmail.com"
                this.subject = "Mail subject"
                this.content = "Lorem ipsum dolor sit amet."
            }
            this.creationTime = Instant.now().toTimestamp()
            this.updateTime = Instant.now().toTimestamp()
            this.status = "SENT"
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
    val server = EmailRepositoryServer(port)
    server.start()
    server.blockUntilShutdown()
}
