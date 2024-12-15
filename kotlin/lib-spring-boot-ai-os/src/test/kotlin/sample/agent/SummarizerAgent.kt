package sample.agent

import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.UnstructuredBaseAgent
import com.github.frtu.kotlin.tool.ToolRegistry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier(SummarizerAgent.TOOL_NAME)
class SummarizerAgent(
    // Chat engine
    chat: Chat,
    // For execution
    toolRegistry: ToolRegistry? = null,
) : UnstructuredBaseAgent(
    id = "summarizer-agent",
    description = "Agent summarising content",
    instructions = "Summarize the current discussion, focusing on the main topics and specific details provided. Highlight the user’s requests, objectives, and preferences, including any recurring themes or ongoing projects. Ensure the summary is concise, accurate, and structured to give a clear overview of the context and progress of the conversation.",
    chat = chat,
    toolRegistry = toolRegistry,
    isStateful = true,
) {
    companion object {
        const val TOOL_NAME = "summarizer-agent"
    }
}
