package com.github.frtu.kotlin.spring.slack.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.slack.app")
data class SlackAppProperties(
    val token: String,
    val signingSecret: String,
    val botOauthToken: String,
)