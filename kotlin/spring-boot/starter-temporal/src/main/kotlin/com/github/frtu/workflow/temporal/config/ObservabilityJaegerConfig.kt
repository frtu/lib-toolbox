package com.github.frtu.workflow.temporal.config

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.key
import com.github.frtu.workflow.temporal.observability.JaegerUtils
import io.jaegertracing.internal.JaegerSpanContext
import io.temporal.opentracing.OpenTracingClientInterceptor
import io.temporal.opentracing.OpenTracingOptions
import io.temporal.opentracing.OpenTracingWorkerInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Build Jaeger connection if find JaegerSpanContext in classpath and 'observability.jaeger.enabled=true'
 *
 * @author Frédéric TU
 * @since 1.2.2
 */
@Configuration
@ConditionalOnProperty(name = ["observability.jaeger.enabled"], havingValue = "true", matchIfMissing = false)
@ConditionalOnClass(JaegerSpanContext::class)
class ObservabilityJaegerConfig {
    @Bean
    fun tracingOptions(
        @Value("\${spring.application.name}")
        applicationName: String,
        @Value("\${observability.jaeger.endpoint}")
        jaegerEndpoint: String,
    ): OpenTracingOptions {
        structuredLogger.info(key("applicationName", applicationName), key("jaegerEndpoint", jaegerEndpoint))
        return JaegerUtils.getJaegerOptions(applicationName, jaegerEndpoint)
    }

    @Bean
    fun tracingClientInterceptor(tracingOptions: OpenTracingOptions) = OpenTracingClientInterceptor(tracingOptions)

    @Bean
    fun tracingWorkerInterceptor(tracingOptions: OpenTracingOptions) = OpenTracingWorkerInterceptor(tracingOptions)

    private val structuredLogger = StructuredLogger.create(this::class.java)
}