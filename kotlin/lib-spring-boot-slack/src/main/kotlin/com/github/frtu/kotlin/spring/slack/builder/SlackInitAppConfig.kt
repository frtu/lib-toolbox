package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.config.SlackProperties
import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class SlackInitAppConfig {
    @Bean
    fun appConfig(slackProperties: SlackProperties) = AppConfig.builder()
        .singleTeamBotToken(slackProperties.botOauthToken())
        .signingSecret(slackProperties.signingSecret())
        .build()

    @Bean
    @Qualifier(KEY_APP)
    fun defaultApp(appConfig: AppConfig): App = App(appConfig)

    @Bean
    @Qualifier(KEY_APP_TOKEN)
    fun defaultAppToken(slackProperties: SlackProperties): String = slackProperties.defaultAppToken()

    /**
     * BotID is available using `ctx.botUserId`. If needed elsewhere, Can get injected using @Qualifier(KEY_APP_BOT_ID)
     */
    @Lazy
    @Bean
    @Qualifier(KEY_APP_BOT_ID)
    fun botId(
        slackProperties: SlackProperties,
        app: App,
    ): String = app.client().authTest { r -> r.token(slackProperties.botOauthToken()) }.userId

    companion object {
        const val KEY_APP = "APP_QUALIFIER"
        const val KEY_APP_TOKEN = "APP_TOKEN_QUALIFIER"
        const val KEY_APP_BOT_ID = "APP_BOT_ID_QUALIFIER"
    }
}