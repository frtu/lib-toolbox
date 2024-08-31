package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.spring.slack.core.SlackLifecycleManagement
import com.github.frtu.kotlin.spring.slack.core.SlackLifecycleManagement.Companion.KEY_APP_TOKEN
import com.slack.api.bolt.AppConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@EnableConfigurationProperties(SlackAppProperties::class)
@Import(SlackLifecycleManagement::class)
class SlackConfig {
    @Bean
    fun appConfig(slackAppProperties: SlackAppProperties) = AppConfig.builder()
        .singleTeamBotToken(slackAppProperties.botOauthToken)
        .signingSecret(slackAppProperties.signingSecret)
        .build()

    @Bean
    @Qualifier(KEY_APP_TOKEN)
    fun defaultAppToken(slackAppProperties: SlackAppProperties): String = slackAppProperties.token
}