package com.github.frtu.kotlin.spring.slack.tool

import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import com.slack.api.bolt.handler.builtin.SlashCommandHandler
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

class ToolCommandFactory {
    fun tool(toolRegistry: ToolRegistry): SlashCommandHandler = SlashCommandHandler { req, ctx ->
        ctx.logger.debug("Command /tool called")
        val commandArgText = req.payload.text
        val args = commandArgText.split(" ")

        val toolName = retrieveToolName(args)
        if (toolName == "list") {
            val text = getToolNames(toolRegistry)
            return@SlashCommandHandler ctx.ack("List of all tool names [ $text ]")
        }

        val text = if (toolName != null && toolName.trim().isNotBlank()) {
            val tool: Tool? = toolRegistry[toolName]
            if (tool != null) {
                val request = retrieveParameters(args, toolName)
                ctx.logger.info("Trying to call tool id:$toolName args[1]=$request")
                val result = runBlocking {
                    tool.execute(request.toJsonNode())
                }
                result.toJsonString()
            } else {
                "Trying to call an tool name that doesn't exist : name=[`$toolName`]. " +
                        "Existing tool names [ ${getToolNames(toolRegistry)} ]"
            }
        } else {
            "Usage: You need to call `/tool [tool-name] [args]`. Existing tool names [ ${getToolNames(toolRegistry)} ]"
        }
        val channelId = req.payload.channelId
        val channelName = req.payload.channelName
        ctx.logger.debug("Command /tool called : [$text] at <#$channelId|$channelName>")
        ctx.ack(text)
    }

    fun getToolNames(toolRegistry: ToolRegistry) = toolRegistry.getAll()
        .joinToString(" | ") { "`${it.id.value}`" }

    fun retrieveToolName(args: List<String>): String = args[0]

    fun retrieveParameters(args: List<String>, toolName: String): String =
        args.findLast { it.isNotBlank() }?.replace("`", "")
            ?: throw IllegalArgumentException("Need to pass non empty parameters to name=[`$toolName`].")
}