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
    /** Category name */
    parameterJsonSchema: String,
    /** Sub category name */
    returnJsonSchema: String? = null,
    /** Category name */
    category: String? = null,
    /** Sub category name */
    subCategory: String? = null,
    /** System instruction prompt */
    instructions: String? = null,
    /** Engine containing model version */
    protected val chat: Chat,
    /** If Agent should keep conversation across Q&A */
    isStateful: Boolean = false,
) : ToolExecuter(
    id = id,
    description = description,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
    category = category,
    subCategory = subCategory,
) {
    //========================================
    /** Original prompt */
    private val _instructions: String? = instructions

    /**
     * Computed prompt - Can be called very early in the construction lifecycle.
     * Make sure it only rely on early init var
     */
    open val instructions: String
        get() = _instructions
            ?: throw IllegalStateException("instructions MUST NOT be null. You need to pass an `instructions` using class constructor OR `override val instructions: String`#get")

    //========================================
    /** Internal state to maintain */
    private var _statefulConversation: Conversation? = null

    /**
     * Start / Init a new `Conversation`
     */
    fun startConversation() {
        logger.info("Creating Agent class:{} with instruction:\n{}", this.javaClass, instructions)
        _statefulConversation = Conversation(instructions)
    }

    /**
     * Returning internal `Conversation` or create a new conversion every time
     */
    val defaultConversation: Conversation
        get() = _statefulConversation ?: Conversation(_instructions).also {
            logger.info("Creating Agent class:{} with instruction:\n{}", this.javaClass, instructions)
        }
    //========================================

    init {
        if (isStateful) {
            startConversation()
        }
    }

    /**
     * Answering question
     */
    open suspend fun answer(
        request: String,
        /** Pass a stateful conversation to keep history */
        conversationOverride: Conversation? = null,
    ): Answer {
        // Use by priority : parameter, else stateful, else create a new
        val conversation = conversationOverride ?: defaultConversation
        return with(conversation) {
            logger.debug("Conversation - Receiving message:$request on history size:${conversation.countMessages()}")
            chat.sendMessage(user(request)).also { answer: Answer ->
                logger.debug("Receiving response:$answer")
            }
        }
    }
}