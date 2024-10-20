package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.spring.slack.builder.SlackInitAppConfig
import com.github.frtu.kotlin.spring.slack.builder.SlackRegisterCommandConfig
import com.github.frtu.kotlin.spring.slack.builder.SlackRegisterEventConfig
import com.github.frtu.kotlin.spring.slack.core.SlackCommandRegistry
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    SlackInitAppConfig::class,
    SlackRegisterCommandConfig::class,
    SlackRegisterEventConfig::class,
)
@EnableConfigurationProperties(SlackAppProperties::class)
@ComponentScan(basePackageClasses = [SlackCommandRegistry::class])
class SlackAutoConfigs