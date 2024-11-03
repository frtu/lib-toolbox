package com.github.frtu.kotlin.tool.execution.durable.spring.config

import com.github.frtu.kotlin.tool.execution.durable.temporal.ToolTemporal
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Allow to bootstrap Tool execution in Spring Boot.
 *
 * Note : NOT USED at the moment
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
@Configuration
@ComponentScan(basePackageClasses = [ToolTemporal::class])
class DurableAgentAutoConfigs