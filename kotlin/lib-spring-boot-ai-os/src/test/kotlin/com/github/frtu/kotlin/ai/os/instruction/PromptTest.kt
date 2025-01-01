package com.github.frtu.kotlin.ai.os.instruction

import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import com.github.frtu.kotlin.ai.feature.intent.model.Intent

class PromptTest {
    @Test
    fun `Render Intent`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        val intents = listOf(
            Intent(id = "Delivery status", description = "Inquiries about the current status of a delivery."),
            Intent(id = "Unblock delivery", description = "Delivery is blocked and need to call API to unblock."),
        )
        val prompt = Prompt("""
            You’re a LLM that detects intent from user queries. Your task is to classify the user's intent based on their query. 
            Below are the possible intents with brief descriptions. Use these to accurately determine the user's goal, and output only the intent topic.
            {{#intents}}
            * {{id}} -> {{description}}
            {{/intents}}
            * Other -> Choose this if the intent doesn't fit into any of the above categories
    
            You are given an utterance and you have to classify it into an intent. 
            It's a matter of life and death, only respond with the intent in the following list
            List:[{{#intents}}{{id}},{{/intents}}Other]
            
            Response format should be a JSON with intent and reasoning explanation.
            Ex : {"intent": "Other", "reasoning": "1. The user wants to put money into stocks, which is a form of investment. 2. They're asking about options, seeking advice on investment choices."}
        """.trimIndent()
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = prompt.render(mapOf("intents" to intents))
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            // Must render text with intents inside it
            this shouldContain intents[0].id
            this shouldContain intents[0].description
            this shouldContain intents[1].id
            this shouldContain intents[1].description
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}