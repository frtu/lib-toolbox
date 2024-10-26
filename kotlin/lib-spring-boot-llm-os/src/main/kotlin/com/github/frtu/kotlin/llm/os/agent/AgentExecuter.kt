package com.github.frtu.kotlin.llm.os.agent

import com.github.frtu.kotlin.llm.os.llm.Chat
import com.github.frtu.kotlin.llm.os.llm.model.Answer
import com.github.frtu.kotlin.llm.os.memory.Conversation
import com.github.frtu.kotlin.llm.os.tool.ToolRegistry
import com.github.frtu.kotlin.utils.io.toJsonNode

/**
 * An agent implementation knowing how to execute tools
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
abstract class AgentExecuter(
    /** Name */
    name: String,
    /** Description */
    description: String,
    /** System instruction prompt */
    instructions: String,
    /** Engine containing model version */
    chat: Chat,
    parameterJsonSchema: String,
    returnJsonSchema: String? = null,
    isStateful: Boolean = false,
    /** For function / tool execution */
    protected val toolRegistry: ToolRegistry? = null,
) : AbstractAgent(
    name = name,
    description = description,
    instructions = instructions,
    chat = chat,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
    isStateful = isStateful,
) {
    override suspend fun answer(
        request: String,
        /** Pass a stateful conversation to keep history */
        conversationOverride: Conversation?,
    ): Answer {
        // Use by priority : parameter, else stateful, else create a new
        val conversation = conversationOverride ?: statefulConversation ?: Conversation(instructions)
        var intermediateAnswer = super.answer(request, conversation)

        val functionCall = intermediateAnswer.message.functionCall
        return if (functionCall != null) {
            val functionName = functionCall.name
            val functionArgs = functionCall.arguments.toJsonNode()
            logger.debug("Function call request:${functionArgs.toPrettyString()}")

            toolRegistry?.get(functionName)?.let { tool ->
                // Maintain Conversation when tool call is needed
                with(conversation) {
                    val message = intermediateAnswer.message
                    this.addResponse(message)

                    val result = tool.execute(functionArgs)
                    val secondResponse = chat.sendMessage(
                        function(
                            functionName = functionName,
                            content = result.textValue()
                        )
                    )
                    logger.debug("2nd response:${secondResponse.message.content}")
                    secondResponse
                }
            }
                ?: throw IllegalStateException("Requesting calling tool:$functionCall that doesn't exist using ${functionArgs.toPrettyString()}")
        } else if (intermediateAnswer.hasToolCall) {
            intermediateAnswer
        } else if (intermediateAnswer.content != null) {
            intermediateAnswer
        } else {
            throw IllegalStateException("answer.content is null but no function call detected!").also {
                logger.error("Error with answer:$intermediateAnswer", it)
            }
        }
    }
}