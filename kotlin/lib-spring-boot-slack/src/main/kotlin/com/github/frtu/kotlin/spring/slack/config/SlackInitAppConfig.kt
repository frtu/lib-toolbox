package com.github.frtu.kotlin.spring.slack.config

import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SlackAppProperties::class)
class SlackInitAppConfig {
    @Bean
    fun appConfig(slackAppProperties: SlackAppProperties) = AppConfig.builder()
        .singleTeamBotToken(slackAppProperties.botOauthToken)
        .signingSecret(slackAppProperties.signingSecret)
        .build()

    @Bean
    @Qualifier(KEY_APP)
    fun defaultApp(appConfig: AppConfig): App = App(appConfig)

    @Bean
    @Qualifier(KEY_APP_TOKEN)
    fun defaultAppToken(slackAppProperties: SlackAppProperties): String = slackAppProperties.token

    companion object {
        const val KEY_APP = "APP_QUALIFIER"
        const val KEY_APP_TOKEN = "APP_TOKEN_QUALIFIER"
    }
}