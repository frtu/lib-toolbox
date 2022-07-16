package com.github.frtu.sample.config

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import java.time.Duration
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import net.devh.boot.grpc.server.metric.MetricCollectingServerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ObservabilityConfig {
    @Bean
    @Primary
    fun primaryMeterRegistry(): CompositeMeterRegistry = Metrics.globalRegistry

    @GrpcGlobalServerInterceptor
    fun serverLogGrpcInterceptor(meterRegistry: MeterRegistry) = MetricCollectingServerInterceptor(meterRegistry)

    @Value("\${spring.application.name:application-name}")
    lateinit var applicationName: String

    @Bean
    fun metricsCommonTags(meterFilter: MeterFilter): MeterRegistryCustomizer<MeterRegistry> =
        MeterRegistryCustomizer { registry: MeterRegistry ->
            // Aligned with https://grafana.com/grafana/dashboards/4701
            logger.info("METRICS - Adding application='{}' to MeterRegistry", applicationName)
            registry.config()
                .commonTags("application", applicationName)
                .meterFilter(meterFilter)
        }

    @Bean
    fun meterFilter() = object : MeterFilter {
        override fun configure(id: Meter.Id, config: DistributionStatisticConfig): DistributionStatisticConfig =
            if (id.name.startsWith("grpc.server.processing") // Adding P99 for gRPC
                || id.name.startsWith("spring.data.repository") // Adding P99 for Repository
            ) {
                logger.debug("METRICS - Id:${id} -> adding P99")
                DistributionStatisticConfig.builder()
                    .percentilesHistogram(true)
                    .percentiles(0.99)
                    .sla(
                        Duration.ofMillis(50).toNanos(),
                        Duration.ofMillis(200).toNanos(),
                        Duration.ofSeconds(1).toNanos()
                    )
                    .minimumExpectedValue(50.0)
                    .maximumExpectedValue(500.0)
                    .build()
                    .merge(config)
            } else {
                config
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}