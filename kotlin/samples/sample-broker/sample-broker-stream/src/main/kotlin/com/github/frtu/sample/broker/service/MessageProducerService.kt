package com.github.frtu.sample.broker.service

import com.github.frtu.sample.persistence.r2dbc.basic.Email
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service
class MessageProducerService {
    private val unicastProcessor = Sinks.many().unicast().onBackpressureBuffer<Message<Email>>()

    fun getProducer() = unicastProcessor

    suspend fun sendMessage(email: Email) {
        val message = MessageBuilder.withPayload(email).build()
        unicastProcessor.emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST)
    }
}
