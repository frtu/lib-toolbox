package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.command.LongRunningSlashCommandHandler
import com.github.frtu.kotlin.spring.slack.command.UserInputToJsonNodeTranslator
import com.github.frtu.kotlin.spring.slack.tool.ToolCommandFactory
import com.github.frtu.kotlin.spring.slack.tool.ToolExecutorHandler
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.translate.TextToJsonNodeTranslator
import com.slack.api.bolt.App
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    prefix = "application.tools.slack", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
// If class exist scan packages
@ConditionalOnClass(ToolRegistry::class)
@ComponentScan(basePackageClasses = [ToolRegistry::class])
class SlackRegisterCommandForToolConfig {
    @Bean
    @ConditionalOnMissingBean
    fun textToJsonNodeTranslator(): TextToJsonNodeTranslator = UserInputToJsonNodeTranslator()

    @Bean
    fun tool(toolRegistry: ToolRegistry, textToJsonNodeTranslator: TextToJsonNodeTranslator) =
        ToolCommandFactory(toolRegistry, textToJsonNodeTranslator).tool()

    @Bean
    @Qualifier("SlackCommandRegistryForToolRegistration")
    fun slackCommandRegistryForToolRegistration(
        app: App,
        toolRegistry: ToolRegistry,
        textToJsonNodeTranslator: TextToJsonNodeTranslator,
    ): String {
        // Register all commands available as Spring Beans
        toolRegistry.getAll().forEach { tool ->
            app.command(
                "/${tool.id.value}", LongRunningSlashCommandHandler(
                    executorHandler = ToolExecutorHandler(tool, textToJsonNodeTranslator),
                    defaultStartingMessage = "Starting execution for tool:${tool.id.value}",
                )
            )
        }
        return "OK"
    }
}