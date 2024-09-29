package com.github.frtu.kotlin.spring.slack.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    SlackInitAppConfig::class,
    SlackRegisterCommandConfig::class,
    SlackRegisterEventConfig::class,
)
@ComponentScan("com.github.frtu.kotlin.spring.slack")
class SlackAutoConfigs