package com.github.frtu.sample.sink.kafka

import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.sink.config.KafkaConfiguration
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.time.Duration
import java.util.concurrent.TimeUnit

@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
@SpringBootTest(
    classes = [EmbeddedKafkaIntegrationTest.ApplicationTest::class],
    properties = [
        "spring.kafka.bootstrap-servers=\${spring.embedded.kafka.brokers}",
        "grpc.server.port: -1",
        "spring.flyway.enabled=false",
    ]
)
@DirtiesContext
@DisplayName("Test for EmailKafkaSink")
@ExtendWith(MockKExtension::class)
class EmbeddedKafkaIntegrationTest {
    // TEST SUBJECT
    @Autowired
    lateinit var emailKafkaSink: EmailKafkaSink

    @Autowired
    lateinit var eventCapture: EventCapture<String, EmailDetail>

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    fun givenEmbeddedKafkaBroker_checkMesssageReceivedInApprovalExecutionRequestKafkaConsumer() = runBlocking {
        // --------------------------------------
        // 1. Prepare server data & Init client
        // --------------------------------------
        val event = EmailDetail(
            "rndfred@gmail.com", "Mail subject",
            "Lorem ipsum dolor sit amet.", "SENT"
        )

        eventCapture.reset()

        // --------------------------------------
        // 2. Execute
        // --------------------------------------
        emailKafkaSink.emit(event)

        await atMost Duration.ofMinutes(1) until {
            eventCapture.capturedList.isNotEmpty()
        }
        logger.debug("result:${eventCapture.capturedList}")

        // --------------------------------------
        // 3. Validate
        // --------------------------------------
        assertThat(eventCapture.capturedList.size).isGreaterThan(0)
        eventCapture.reset()
    }

    @EnableKafka
    @Configuration
    class BoostrapEmbeddedKafkaConfiguration : KafkaConfiguration() {
        @Autowired
        lateinit var embeddedKafka: EmbeddedKafkaBroker

        @Bean
        fun admin(): KafkaAdmin = KafkaAdmin(KafkaTestUtils.producerProps(embeddedKafka))

        // Auto create topics
        @Bean
        fun topicSource(): NewTopic = TopicBuilder
            .name(outputSource)
            .partitions(1)
            .replicas(1)
            .build()

        @Bean
        @Primary
        fun overrideKafkaTemplate(properties: KafkaProperties, embeddedKafka: EmbeddedKafkaBroker): ReactiveKafkaProducerTemplate<String, EmailDetail> {
            val producerProperties = properties.buildProducerProperties().apply {
                this["bootstrap.servers"] = embeddedKafka.brokersAsString
                this["value.serializer"] = "org.springframework.kafka.support.serializer.JsonSerializer"
            }
            logger.info("== KAFKA CONFIG: {}", producerProperties)
            return reactiveKafkaProducerTemplateMap(producerProperties)
        }

        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    class EventCapture<K, V>(val capturedList: MutableList<V> = mutableListOf()) {
        @KafkaListener(
            topics = ["\${application.topic.domain-source}"],
            groupId = "source",
            properties = ["auto.offset.reset=earliest"],
        )
        fun capture(@Payload consumerRecord: ConsumerRecord<K, V>) {
            val value = consumerRecord.value()
            capturedList.add(value)
        }

        fun reset() {
            capturedList.clear()
        }
    }

    @SpringBootApplication
    @ActiveProfiles("test")
    @Import(BoostrapEmbeddedKafkaConfiguration::class)
    class ApplicationTest {
        @Bean
        fun eventCapture() = EventCapture<String, EmailDetail>()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
