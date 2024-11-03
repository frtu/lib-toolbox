package com.github.frtu.kotlin.tool.execution.durable.spring.config

import com.github.frtu.kotlin.tool.execution.durable.temporal.ToolTemporal
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Allow to bootstrap AI OS configuration
 */
@Configuration
@ComponentScan(basePackageClasses = [ToolTemporal::class])
class DurableAgentAutoConfigs