package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.utils.data.ValueObject
import org.springframework.boot.context.properties.ConfigurationProperties

@ValueObject
@ConfigurationProperties("application.slack.app")
data class SlackAppProperties(
    val token: String,
    val signingSecret: String,
    val botOauthToken: String,
)