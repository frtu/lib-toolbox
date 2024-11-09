package com.github.frtu.kotlin.ai.spring.config

import com.github.frtu.kotlin.ai.os.AiOS
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import com.github.frtu.kotlin.tool.service.rpc.WebhookWebfluxRouter
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
@ComponentScan(basePackageClasses = [AiOS::class])
class LlmOsAutoConfigs