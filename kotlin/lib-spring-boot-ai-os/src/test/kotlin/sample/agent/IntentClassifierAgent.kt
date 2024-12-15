package sample.agent

import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.UnstructuredBaseAgent
import com.github.frtu.kotlin.tool.ToolRegistry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier(IntentClassifierAgent.TOOL_NAME)
class IntentClassifierAgent(
    // Chat engine
    chat: Chat,
    // For execution
    toolRegistry: ToolRegistry? = null,
) : UnstructuredBaseAgent(
    id = TOOL_NAME,
    description = "Agent that classify user request into category",
    instructions = """
        You’re a LLM that detects intent from user queries. Your task is to classify the user's intent based on their query. Below are the possible intents with brief descriptions. Use these to accurately determine the user's goal, and output only the intent topic.
        - Order Status: Inquiries about the current status of an order, including delivery tracking and estimated arrival times.
        - Product Information: Questions regarding product details, specifications, availability, or compatibility.
        - Payments: Queries related to making payments, payment methods, billing issues, or transaction problems.
        - Returns: Requests or questions about returning a product, including return policies and procedures.
        - Feedback: User comments, reviews, or general feedback about products, services, or experiences.
        - Other: Choose this if the query doesn’t fall into any of the other intents.
    """,
    chat = chat,
    toolRegistry = toolRegistry,
    isStateful = true,
) {
    companion object {
        const val TOOL_NAME = "intent-classifier-agent"
    }
}