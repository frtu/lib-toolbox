package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.utils.data.ValueObject
import org.springframework.boot.context.properties.ConfigurationProperties

@ValueObject
@ConfigurationProperties(prefix = "application.slack")
data class SlackProperties(
    val enabled: Boolean = true,
    private var app: SlackAppProperties?,
) {
    fun defaultAppToken(): String = app!!.token

    fun botOauthToken(): String = app!!.botOauthToken
    fun signingSecret(): String = app!!.signingSecret
}