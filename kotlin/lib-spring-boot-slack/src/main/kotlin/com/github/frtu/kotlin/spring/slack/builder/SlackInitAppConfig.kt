package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.config.SlackProperties
import com.github.frtu.kotlin.spring.slack.core.SlackApp
import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class SlackInitAppConfig(
    slackProperties: SlackProperties
) {
    private val defaultApp = SlackApp(slackProperties.defaultApp())

    @Bean
    @Qualifier(KEY_APP)
    fun defaultApp(): App = defaultApp.app

    @Bean
    @Qualifier(KEY_APP_TOKEN)
    fun defaultAppToken(): String = defaultApp.appToken()

    /**
     * BotID is available using `ctx.botUserId`. If needed elsewhere, Can get injected using @Qualifier(KEY_APP_BOT_ID)
     */
    @Lazy
    @Bean
    @Qualifier(KEY_APP_BOT_ID)
    fun defaultBotId(): String = defaultApp.botId()

    companion object {
        const val KEY_APP = "APP_QUALIFIER"
        const val KEY_APP_TOKEN = "APP_TOKEN_QUALIFIER"
        const val KEY_APP_BOT_ID = "APP_BOT_ID_QUALIFIER"
    }
}