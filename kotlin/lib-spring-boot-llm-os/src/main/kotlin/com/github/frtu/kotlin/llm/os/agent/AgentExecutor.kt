package com.github.frtu.kotlin.llm.os.agent

import com.github.frtu.kotlin.llm.os.llm.Chat
import com.github.frtu.kotlin.llm.os.llm.model.Answer
import com.github.frtu.kotlin.llm.os.memory.Conversation
import com.github.frtu.kotlin.llm.os.tool.Tool
import com.github.frtu.kotlin.llm.os.tool.function.FunctionRegistry
import com.github.frtu.kotlin.utils.io.toJsonNode

/**
 * An agent implementation knowing how to execute tools
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
abstract class AgentExecutor(
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
    protected val toolRegistry: FunctionRegistry? = null,
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
            // Maintain Conversation when tool call is needed
            with(conversation) {
                val message = intermediateAnswer.message
                this.addResponse(message)

                val functionName = functionCall.name

                val functionToCall: Tool = toolRegistry!!.getFunction(functionName)
                val functionArgs = functionCall.arguments.toJsonNode()
                logger.debug("Request:${functionArgs.toPrettyString()}")

                val result = functionToCall.execute(functionArgs)
                val secondResponse = chat.sendMessage(
                    function(
                        functionName = functionName,
                        content = result.textValue()
                    )
                )
                logger.debug("2nd response:${secondResponse.message.content}")
                secondResponse
            }
        } else if (intermediateAnswer.content != null) {
            intermediateAnswer
        } else {
            throw IllegalStateException("answer.content is null but no function call detected!").also {
                logger.error("Error with answer:$intermediateAnswer", it)
            }
        }
    }
}