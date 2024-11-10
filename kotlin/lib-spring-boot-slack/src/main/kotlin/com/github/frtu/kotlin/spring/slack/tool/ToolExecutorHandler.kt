package com.github.frtu.kotlin.spring.slack.tool

import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.spring.slack.command.ExecutorHandler
import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.translate.TextToJsonNodeTranslator
import com.github.frtu.kotlin.utils.io.toJsonString
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import org.slf4j.Logger

class ToolExecutorHandler(
    /** Containing suspendable execution logic */
    private val tool: Tool,
    private val textToJsonNodeTranslator: TextToJsonNodeTranslator,
) : ExecutorHandler {
    override suspend fun invoke(req: SlashCommandRequest, ctx: SlashCommandContext, logger: Logger): String? {
        val toolName = tool.id.value

        val request = textToJsonNodeTranslator.execute(req.payload.text)
        val text = if (request is NullNode) {
            throw IllegalArgumentException("Need to pass non empty parameters to name=[`$toolName`].")
        } else {
            ctx.logger.info("Trying to call tool id:$toolName args=$request")
            tool.execute(request).toJsonString()
        }
        val channelId = req.payload.channelId
        val channelName = req.payload.channelName
        ctx.logger.debug("Command /tool called : [$text] at <#$channelId|$channelName>")
        return text
    }
}