package sample.agent

import com.github.frtu.kotlin.ai.os.instruction.Prompt
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.StructuredBaseAgent
import com.github.frtu.kotlin.ai.os.llm.agent.UnstructuredBaseAgent
import com.github.frtu.kotlin.serdes.json.ext.toJsonObj
import com.github.frtu.kotlin.tool.ToolRegistry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import sample.agent.model.Intent
import sample.agent.model.IntentResult

@Component
@Qualifier(IntentClassifierAgent.TOOL_NAME)
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
        get() = Prompt(
        template = """
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
    ).render(mapOf("intents" to intents))

    companion object {
        const val TOOL_NAME = "intent-classifier-agent"

        // Intents
        const val DEFAULT_INTENT_ID = "Other"
    }
}