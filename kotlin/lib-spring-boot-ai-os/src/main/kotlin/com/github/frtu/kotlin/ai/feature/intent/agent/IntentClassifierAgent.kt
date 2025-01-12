package com.github.frtu.kotlin.ai.feature.intent.agent

import com.github.frtu.kotlin.ai.feature.intent.model.Intent
import com.github.frtu.kotlin.ai.feature.intent.model.IntentResult
import com.github.frtu.kotlin.ai.os.instruction.PromptTemplate
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.StructuredBaseAgent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 * Agent that classify user request into Intent classification
 * @since 2.0.13
 */
class IntentClassifierAgent(
    // Chat engine
    chat: Chat,
    // All Intents
    val intents: List<Intent>,
) : StructuredBaseAgent<String, IntentResult>(
    id = TOOL_NAME,
    description = "Agent that classify user request into Intent classification",
    parameterClass = String::class.java,
    returnClass = IntentResult::class.java,
    chat = chat,
) {
    override val instructions: String
        get() = PromptTemplate(TOOL_NAME,
            """
            You’re a LLM that detects intent from user queries. Your task is to classify the user's intent based on their query. 
            Below are the possible intents with brief descriptions. Use these to accurately determine the user's goal, and output only the intent topic.
            {{#intents}}
            * {{id}} -> {{description}}
            {{/intents}}
            * $DEFAULT_INTENT_ID -> Choose this if the intent doesn't fit into any of the above categories
            
            You are given an utterance and you have to classify it into an intent. 
            It's a matter of life and death, only respond with the intent in the following list
            List:[{{#intents}}{{id}},{{/intents}}$DEFAULT_INTENT_ID]
            
            Response format MUST in JSON format with intent and non empty reasoning explanation.
            Ex : {"intent": "$DEFAULT_INTENT_ID", "reasoning": "1. The user wants to put money into stocks, which is a form of investment. 2. They're asking about options, seeking advice on investment choices."}
            """.trimIndent(),
        ).format(mapOf("intents" to intents))

    override suspend fun execute(parameter: String): IntentResult {
        val (intent, reasoning) = super.execute(parameter)
        return with(intent.trim()) {
            if (this == DEFAULT_INTENT_ID) {
                logger.debug("Returned Intent fall into category `Other`")
                IntentResult(DEFAULT_INTENT_ID, reasoning)
            } else if (intentIds.contains(this)) {
                logger.debug("Returned Intent direct match with `$this`")
                IntentResult(this, reasoning)
            } else {
                // Check if returned `intent` is part of the one provided
                val blurMatch =
                    intentIds.find { originalIntent: String -> originalIntent.lowercase() == this.lowercase() }
                logger.debug("Returned Intent blur match with `$this` <=> `$blurMatch`")
                blurMatch?.let { IntentResult(blurMatch, reasoning) }
                    ?: throw IllegalStateException(
                        "LLM returned:[$this] which doesn't match any proposed intent:[${
                            intents.joinToString(
                                separator = ","
                            ) { it.id }
                        }]"
                    )
            }
        }
    }

    protected val intentIds: List<String> = intents.map { it.id }

    /**
     * Make sure all Intent follow best practices!
     * When possible raise `IllegalArgumentException` with more specific details on problem and how to resolve it.
     */
    open fun validateIntents(intents: List<Intent>) {
        if (intents.isEmpty()) throw IllegalArgumentException("Intents parameters MUST be valid!")
    }

    init {
        validateIntents(intents)
    }

    companion object {
        const val TOOL_NAME = "intent-classifier-agent"

        // Intents
        const val DEFAULT_INTENT_ID = "Other"
    }
}
