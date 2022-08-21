package com.github.frtu.sample.cloudevents

import io.cloudevents.CloudEvent
import io.cloudevents.kafka.CloudEventDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*
import java.util.function.Consumer


object CloudEventConsumer {
    @JvmStatic
    fun main(args: Array<String>) {
        val props = Properties().apply {
            this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
            this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = CloudEventDeserializer::class.java
        }

        KafkaConsumer<String, CloudEvent>(props).use { consumer ->
            val records = consumer.poll(Duration.ofSeconds(10))
            records.forEach(Consumer { rec: ConsumerRecord<String, CloudEvent> ->
                println(rec.value().toString())
            })
        }
    }
}