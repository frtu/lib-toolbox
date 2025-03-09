package com.github.frtu.kotlin.spring.slack.core

import com.github.frtu.kotlin.spring.slack.config.SlackAppProperties
import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig

class SlackApp(
    val name: String,
    private val slackAppProperties: SlackAppProperties,
) {
    val app: App = App(
        AppConfig.builder()
            .singleTeamBotToken(slackAppProperties.botOauthToken)
            .signingSecret(slackAppProperties.signingSecret)
            .build()
    )

    fun appToken(): String = slackAppProperties.token

    /**
     * BotID is available using `ctx.botUserId`.
     */
    fun botId(): String = app.client().authTest { r -> r.token(slackAppProperties.botOauthToken) }.userId
}