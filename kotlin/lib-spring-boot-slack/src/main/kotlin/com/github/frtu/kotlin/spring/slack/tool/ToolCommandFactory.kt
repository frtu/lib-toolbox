package com.github.frtu.kotlin.spring.slack.tool

import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import com.slack.api.bolt.handler.builtin.SlashCommandHandler
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnBean(ToolRegistry::class)
@ConditionalOnProperty(
    prefix = "application.tools.slack", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
class ToolCommandFactory {
    @Bean
    fun tool(toolRegistry: ToolRegistry): SlashCommandHandler = SlashCommandHandler { req, ctx ->
        ctx.logger.debug("Command /tool called")
        val commandArgText = req.payload.text
        val args = commandArgText.split(" ")
        val toolName = args[0]

        val text = if (toolName != null) {
            val tool: Tool? = toolRegistry[toolName]
            if (tool != null) {
                ctx.logger.debug("Trying to call tool id:$toolName")
                val result = runBlocking {
                    tool.execute(args[1].toJsonNode())
                }
                result.toJsonString()
            } else {
                "Trying to call an tool name that doesn't exist : name=[$toolName]`"
            }
        } else {
            "Usage: You need to call `/tool [tool-name] [args]`"
        }
        val channelId = req.payload.channelId
        val channelName = req.payload.channelName
        ctx.logger.debug("Command /tool called : [$text] at <#$channelId|$channelName>")
        ctx.ack(text)
    }
}