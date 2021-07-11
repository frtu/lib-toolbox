package com.github.frtu.sample.broker.producer

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate

/**
 * Based on :
 * @see <a href="https://docs.spring.io/spring-kafka/reference/html/#introduction">Spring Kafka</a>
 * @author Frédéric TU
 */
@SpringBootApplication
class ProducerApplication {
    @Autowired
    lateinit var producerSource: ProducerSource

    @Bean
    fun topicSource(): NewTopic? {
        return TopicBuilder.name(producerSource.outputSource)
            .partitions(10)
            .replicas(1)
            .build()
    }

    @Bean
    fun runner(): ApplicationRunner? = ApplicationRunner { args: ApplicationArguments? ->
        producerSource.send("domain-message")
    }
}

fun main(args: Array<String>) {
    System.getProperties().put("server.port", 8083);
    runApplication<ProducerApplication>(*args)
}