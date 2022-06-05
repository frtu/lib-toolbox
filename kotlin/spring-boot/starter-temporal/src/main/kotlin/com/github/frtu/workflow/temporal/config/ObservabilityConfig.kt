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
import javax.annotation.PostConstruct

@Configuration
@ConditionalOnProperty(name = ["jaeger.enabled"], havingValue = "true", matchIfMissing = false)
@ConditionalOnClass(JaegerSpanContext::class)
class ObservabilityConfig {
    @Value("\${spring.application.name}")
    lateinit var applicationName: String

    @Value("\${jaeger.endpoint}")
    lateinit var jaegerEndpoint: String

    @PostConstruct
    fun afterInit() {
        structuredLogger.info(key("applicationName", applicationName), key("jaegerEndpoint", jaegerEndpoint))
    }

    @Bean
    fun tracingOptions(): OpenTracingOptions = JaegerUtils.getJaegerOptions(applicationName, jaegerEndpoint)

    @Bean
    fun tracingClientInterceptor(tracingOptions: OpenTracingOptions) = OpenTracingClientInterceptor(tracingOptions)

    @Bean
    fun tracingWorkerInterceptor(tracingOptions: OpenTracingOptions) = OpenTracingWorkerInterceptor(tracingOptions)

    private val structuredLogger = StructuredLogger.create(this::class.java)
}