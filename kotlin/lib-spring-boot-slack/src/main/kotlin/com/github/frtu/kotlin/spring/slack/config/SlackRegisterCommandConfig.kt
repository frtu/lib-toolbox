package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.spring.slack.core.SlackCommandRegistry
import com.slack.api.bolt.App
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SlackRegisterCommandConfig {
    @Bean
    @Qualifier("SlackCommandRegistryRegistration")
    fun slackCommandRegistryRegistration(app: App, slackCommandRegistry: SlackCommandRegistry): String {
        // Register all commands available as Spring Beans
        slackCommandRegistry.getAll().forEach { (name, command) ->
            app.command("/$name", command)
        }
        return "OK"
    }
}