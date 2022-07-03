package com.github.frtu.workflow.temporal.config

import com.uber.m3.tally.RootScopeBuilder
import com.uber.m3.util.Duration
import io.micrometer.core.instrument.MeterRegistry
import io.temporal.common.reporter.MicrometerClientStatsReporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.uber.m3.tally.Scope as MetricScope

/**
 * Build Tally Scope when found in classpath
 *
 * @author Frédéric TU
 * @since 1.2.2
 */
@Configuration
@ConditionalOnClass(MetricScope::class)
class ObservabilityMetricsConfig {
    @Bean
    fun metricScope(
        meterRegistry: MeterRegistry,
        @Value("\${observability.metrics.report.sec:1}")
        reportEverySec: Double,
    ): MetricScope = RootScopeBuilder()
        .reporter(MicrometerClientStatsReporter(meterRegistry))
        .reportEvery(Duration.ofSeconds(reportEverySec))
}