package com.github.frtu.kotlin.ai.spring.config

import com.github.frtu.kotlin.ai.os.AiOS
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Allow to bootstrap AI OS configuration
 */
// Dependencies
@Import(
    ChatApiConfigs::class,
)
@ComponentScan(basePackageClasses = [AiOS::class])
@EnableConfigurationProperties(ChatApiProperties::class)
@Configuration
class LlmOsAutoConfigs