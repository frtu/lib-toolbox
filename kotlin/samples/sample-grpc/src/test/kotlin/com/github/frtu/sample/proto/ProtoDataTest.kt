package com.github.frtu.sample.proto

import com.github.frtu.kotlin.protobuf.utils.toTimestamp
import com.github.frtu.sample.grpc.EmailEvent
import com.github.frtu.sample.grpc.email
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.util.JsonFormat
import org.junit.jupiter.api.Test
import java.time.Instant

class ProtoDataTest {
    @Test
    @Throws(InvalidProtocolBufferException::class)
    fun printJSON() {
        // Using basic Builder syntax
        val sample = EmailEvent.newBuilder()
            .setData(
                // Using Kotlin DSL syntax
                email {
                    this.id = 1234
                    this.receiver = "rndfred@gmail.com"
                    this.subject = "Mail subject"
                    this.content = "Lorem ipsum dolor sit amet."
                }
            )
            .setEventTime(Instant.now().toTimestamp())
            .setStatus("SENT")
            .build()

        val json = JsonFormat.printer()
            .preservingProtoFieldNames()
            .includingDefaultValueFields()
            .print(sample)
        println(json)
    }
}
