package com.github.frtu.sample.config

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import net.devh.boot.grpc.server.metric.MetricCollectingServerInterceptor
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
}