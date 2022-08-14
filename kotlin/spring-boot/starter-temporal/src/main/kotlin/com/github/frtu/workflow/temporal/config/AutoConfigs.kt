package com.github.frtu.workflow.temporal.config

import io.temporal.client.WorkflowClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Autoconfigure Temporal connectivity, Metrics, Jaeger, ...
 *
 * @author Frédéric TU
 * @since 1.2.1
 */
@Configuration
@Import(TemporalConfig::class, WorkerRegistrationConfig::class, ObservabilityJaegerConfig::class)
@EnableConfigurationProperties(TemporalStubProperties::class)
@ConditionalOnClass(WorkflowClient::class)
class AutoConfigs