package com.github.frtu.kotlin.ai.spring.config

import com.github.frtu.kotlin.action.tool.service.rpc.WebhookWebfluxRouter
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Allow to bootstrap AI OS configuration
 */
@Configuration
@Import(
    ChatApiConfigs::class,
    WebhookWebfluxRouter::class,
)
@EnableConfigurationProperties(ChatApiProperties::class)
@ComponentScan("com.github.frtu.kotlin.llm.os")
class LlmOsAutoConfigs