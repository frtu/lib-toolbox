package com.github.frtu.kotlin.ai.spring.config

import com.github.frtu.kotlin.ai.os.AiOS
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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
// Conditional
@Configuration
@ConditionalOnProperty(
    prefix = "application.${LlmOsAutoConfigs.CONFIG_PREFIX}", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
class LlmOsAutoConfigs {
    companion object {
        const val CONFIG_PREFIX = "ai.os.llm"
    }
}
