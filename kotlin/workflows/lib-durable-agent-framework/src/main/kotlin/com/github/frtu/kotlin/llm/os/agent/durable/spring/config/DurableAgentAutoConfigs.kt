package com.github.frtu.kotlin.llm.os.agent.durable.spring.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Allow to bootstrap AI OS configuration
 */
@Configuration
@ComponentScan("com.github.frtu.kotlin.llm.os.agent.durable")
class DurableAgentAutoConfigs