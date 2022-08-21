package com.github.frtu.sample.serverless.cloudevents

import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.kafka.CloudEventSerializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.net.URI
import java.util.*

object CloudEventProducer {
    @JvmStatic
    fun main(args: Array<String>) {
        val props = Properties().apply {
            this[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            this[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = CloudEventSerializer::class.java
        }

        KafkaProducer<String, CloudEvent>(props).use { producer ->
            // Build an event
            val event = CloudEventBuilder.v1()
                .withId("hello")
                .withType("example.kafka")
                .withSource(URI.create("http://localhost"))
                .build()

            // Produce the event
            producer.send(ProducerRecord("your.topic", event))
        }
    }
}