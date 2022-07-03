package com.github.frtu.workflow.temporal.config

import io.temporal.client.WorkflowClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(TemporalConfig::class, ObservabilityConfig::class)
@EnableConfigurationProperties(TemporalStubProperties::class)
@ConditionalOnClass(WorkflowClient::class)
class AutoConfigs