package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.spring.slack.SpringSlack
import com.github.frtu.kotlin.spring.slack.builder.SlackInitAppConfig
import com.github.frtu.kotlin.spring.slack.builder.SlackRegisterCommandConfig
import com.github.frtu.kotlin.spring.slack.builder.SlackRegisterCommandForToolConfig
import com.github.frtu.kotlin.spring.slack.builder.SlackRegisterEventConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    SlackInitAppConfig::class,
    SlackRegisterCommandConfig::class,
    SlackRegisterEventConfig::class,
    SlackRegisterCommandForToolConfig::class,
)
@EnableConfigurationProperties(SlackProperties::class)
@ComponentScan(basePackageClasses = [SpringSlack::class])
class SlackAutoConfigs