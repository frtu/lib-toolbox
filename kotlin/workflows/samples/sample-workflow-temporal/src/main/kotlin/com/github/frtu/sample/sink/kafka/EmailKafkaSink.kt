package com.github.frtu.sample.sink.kafka

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.key
import com.github.frtu.logs.core.StructuredLogger.message
import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.sink.EmailSink
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate

/**
 * To enable it make sure KafkaConfiguration is correctly activated by configuring Kafka properties
 */
class EmailKafkaSink(
    var outputSource: String,
    var kafkaTemplate: ReactiveKafkaProducerTemplate<String, EmailDetail>,
) : EmailSink {
    override suspend fun emit(emailDetail: EmailDetail) {
        structuredLogger.info(key("topic", outputSource), message("Sending event:$emailDetail"))
        kafkaTemplate.send(outputSource, emailDetail).awaitFirstOrNull()
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}
