package com.github.frtu.kotlin.spring.slack.mock

import com.slack.api.bolt.App
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppMockConfig {
    @Bean
    fun app(): App = mockk(relaxed = true)
}