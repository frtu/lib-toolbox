package com.github.frtu.sample.broker.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class ConsumerSource {
    companion object {
        const val inputSource: String = "domain-source"
    }

    val latch = CountDownLatch(1)
    var payload: String? = null

    @KafkaListener(id = "consumer-1", topics = [inputSource])
    fun listen(consumerRecord: ConsumerRecord<Any, Any>) {
        listen(consumerRecord.toString())
    }

    fun listen(input: String) {
        logger.info("received payload='{}'", input);
        this.payload = input
        latch.countDown()
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}