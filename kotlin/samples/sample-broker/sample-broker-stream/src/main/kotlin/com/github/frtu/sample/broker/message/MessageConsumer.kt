package com.github.frtu.sample.broker.message

import com.github.frtu.sample.broker.service.MessageConsumerService
import com.github.frtu.sample.persistence.r2dbc.basic.Email
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
class MessageConsumer constructor(private val service: MessageConsumerService) {
    @Bean
    fun consumeMessage(): Consumer<Flux<Message<Email>>> = Consumer { stream ->
        stream.concatMap { msg ->
            mono { service.processProductMessage(msg.payload) }
        }.onErrorContinue { e, _ ->
            logger.error(e.message, e)
        }.subscribe()
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}
