package com.github.frtu.kotlin.spring.slack.udf

import com.slack.api.bolt.handler.builtin.SlashCommandHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommandFactory {
    @Bean
    fun hello(): SlashCommandHandler = SlashCommandHandler { req, ctx ->
        ctx.logger.debug("Command /hello called")

        val response = ctx.client().chatPostMessage { r ->
            r.channel(ctx.channelId).text(":wave: How are you?")
        }
        println(response.message)
        ctx.ack(":wave: Hello!")
    }

    @Bean
    fun echo(): SlashCommandHandler = SlashCommandHandler { req, ctx ->
        val commandArgText = req.payload.text
        val channelId = req.payload.channelId
        val channelName = req.payload.channelName
        val text = "You said $commandArgText at <#$channelId|$channelName>"
        ctx.logger.debug("Command /echo called : $text")
        ctx.ack(text)
    }
}