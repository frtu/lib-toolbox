package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.core.SlackCommandRegistry
import com.slack.api.bolt.App
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@ConditionalOnProperty(
    prefix = "application.slack.${SlackRegisterCommandConfig.CONFIG_PREFIX}", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
@Configuration
@Import(SlackCommandRegistry::class)
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

    companion object {
        const val CONFIG_PREFIX = "commands"
    }
}