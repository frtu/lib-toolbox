package com.github.frtu.kotlin.spring.slack.command

import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.handler.builtin.SlashCommandHandler
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.response.Response.error
import com.slack.api.bolt.response.Response.ok
import kotlin.concurrent.thread
import kotlinx.coroutines.runBlocking

/**
 * Potentially long-running SlashCommandHandler that #ack() first and let the execution logic finishes before returning.
 */
class LongRunningSlashCommandHandler(
    /** Containing suspendable execution logic */
    private val executorHandler: ExecutorHandler,
    /** Allow to map Exception into StatusCode */
    private val errorHandler: (Exception) -> Int = { 400 },
    /** Message to inform Slack user task is being executed */
    private val defaultStartingMessage: String = "Processing your request...",
    /** Generic message to return to non-technical user. If null return error message */
    private val defaultErrorMessage: String? = null,
) : SlashCommandHandler {
    override fun apply(req: SlashCommandRequest, ctx: SlashCommandContext): Response = runBlocking {
        // Init
        val logger = ctx.logger

        // Immediate response to avoid timeout
        val preliminaryResponse = with(defaultStartingMessage) {
            logger.info(this)
            ctx.ack(this)
        }
        // Fork a parallel execution
        thread {
            try {
                runBlocking {
                    // Long-running task
                    ok(executorHandler.invoke(req, ctx, logger)).also {
                        logger.info("Finished: $it")
                        ctx.respond(it.body)
                    }
                }
            } catch (e: Exception) {
                logger.error(e.message, e)
                ctx.respond(defaultErrorMessage ?: e.message)
                error(errorHandler(e))
            }
        }
        // Return
        preliminaryResponse
    }
}