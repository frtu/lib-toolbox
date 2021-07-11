package com.github.frtu.sample.broker.message

import com.github.frtu.sample.broker.service.MessageProducerService
import com.github.frtu.sample.persistence.r2dbc.basic.Email
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Supplier

@Configuration
class MessageProducer constructor(private val service: MessageProducerService) {
    @Bean
    fun produceMessage(): Supplier<Flux<Message<Email>>> = Supplier {
        logger.info("Calling supplier getProducer()")
        service.getProducer().asFlux()
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}
