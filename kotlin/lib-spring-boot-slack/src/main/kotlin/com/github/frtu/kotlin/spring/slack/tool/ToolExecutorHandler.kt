package com.github.frtu.kotlin.spring.slack.tool

import com.github.frtu.kotlin.spring.slack.command.ExecutorHandler
import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import org.slf4j.Logger

class ToolExecutorHandler(
    /** Containing suspendable execution logic */
    private val tool: Tool,
) : ExecutorHandler {
    override suspend fun invoke(req: SlashCommandRequest, ctx: SlashCommandContext, logger: Logger): String? {
        val toolName = tool.id.value

        val commandArgText = req.payload.text
        val args = commandArgText.split(" ")
        val request = args.findLast { it.isNotBlank() }?.replace("`", "")
            ?: throw IllegalArgumentException("Need to pass non empty parameters to name=[`$toolName`].")
        ctx.logger.info("Trying to call tool id:$toolName args[1]=$request")

        val result = tool.execute(request.toJsonNode())
        val text = result.toJsonString()

        val channelId = req.payload.channelId
        val channelName = req.payload.channelName
        ctx.logger.debug("Command /tool called : [$text] at <#$channelId|$channelName>")
        return text
    }
}