package com.github.frtu.sample.sink.config

import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.sink.kafka.EmailKafkaSink
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.support.serializer.JsonSerializer
import reactor.kafka.sender.SenderOptions

@Configuration
@ConditionalOnProperty(prefix = "spring.kafka", name = ["bootstrap-servers"])
class KafkaConfiguration {
    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServers: String

    @Value("\${application.topic.domain-source}")
    lateinit var outputSource: String

    @Bean
    fun emailKafkaSink(
        kafkaTemplate: ReactiveKafkaProducerTemplate<String, EmailDetail>,
    ) = EmailKafkaSink(outputSource, kafkaTemplate)

    @Bean
    fun reactiveKafkaProducerTemplate(properties: KafkaProperties): ReactiveKafkaProducerTemplate<String, EmailDetail> =
        reactiveKafkaProducerTemplateMap(properties.buildProducerProperties())

    fun reactiveKafkaProducerTemplateMap(producerProperties: MutableMap<String, Any>): ReactiveKafkaProducerTemplate<String, EmailDetail> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(producerProperties))

    fun producerFactory(): ProducerFactory<String, EmailDetail> = DefaultKafkaProducerFactory(
        mutableMapOf<String, Any?>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
        )
    )

    fun kafkaTemplate(): KafkaTemplate<String, EmailDetail> {
        return KafkaTemplate(producerFactory())
    }
}