package com.github.frtu.kotlin.ai.os.llm.agent

import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.model.Answer
import com.github.frtu.kotlin.ai.os.memory.Conversation
import com.github.frtu.kotlin.tool.ToolExecuter

/**
 * An independent entity with a `name`, `description` and have all the core engine `chat` and optionally `functionRegistry`
 * to receive instruction and call `function/tools`.
 *
 * It manages `Conversation` using `isStateful` constructor parameter
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
abstract class AbstractAgent(
    /** Id of the agent */
    id: ActionId,
    /** Description */
    description: String,
    /** System instruction prompt */
    protected val instructions: String,
    /** Engine containing model version */
    protected val chat: Chat,
    parameterJsonSchema: String,
    returnJsonSchema: String? = null,
    isStateful: Boolean = false,
) : ToolExecuter(
    id = id,
    description = description,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
) {
    /**
     * Start a new `Conversation`
     */
    fun startConversation() {
        internalStatefulConversation = Conversation(instructions)
    }

    /**
     * Returning internal `Conversation` if exist
     */
    val statefulConversation: Conversation?
        get() = internalStatefulConversation

    /**
     * Answering question
     */
    open suspend fun answer(
        request: String,
        /** Pass a stateful conversation to keep history */
        conversationOverride: Conversation? = null,
    ): Answer {
        // Use by priority : parameter, else stateful, else create a new
        val conversation = conversationOverride ?: statefulConversation ?: Conversation(instructions)
        return with(conversation) {
            logger.debug("Conversation - Receiving message:$request on history size:${conversation.countMessages()}")
            chat.sendMessage(user(request)).also { answer: Answer ->
                logger.debug("Receiving response:$answer")
            }
        }
    }

    init {
        if (isStateful) {
            startConversation()
        }
    }

    private var internalStatefulConversation: Conversation? = null
}