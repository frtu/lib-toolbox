package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity

import com.github.frtu.kotlin.ai.os.instruction.PromptTemplate
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.AbstractAgent
import com.github.frtu.kotlin.ai.os.llm.agent.UnstructuredBaseAgent
import com.github.frtu.kotlin.tool.ToolRegistry
import kotlinx.coroutines.runBlocking

/**
 * Encapsulate an Agent as an abstract SpecialisedAgentActivity.
 *
 * @author Frédéric TU
 * @since 2.0.16
 */
class BaseSpecialisedAgentActivityImpl(
    private val agent: AbstractAgent,
) : SpecialisedAgentActivity {
    override fun message(userMessage: String): String = runBlocking {
        agent.answer(userMessage).content!!
    }

    fun getId(): String = agent.id.value
    fun getDescription(): String = agent.description
    fun getInstructions(): String = agent.instructions

    companion object {
        /**
         * Facilitator to create a BaseSpecialisedAgentActivityImpl that encapsulate UnstructuredBaseAgent by passing
         * only a PromptTemplate and if necessary prompt input vars
         *
         * @since 2.0.16
         */
        fun create(
            /** Engine containing model version */
            chat: Chat,
            /** Create object from PromptTemplate */
            promptTemplate: PromptTemplate,
            /** Variable needed by prompts */
            input: Map<String, Any> = emptyMap(),
            /** Id of the agent */
            id: String? = null,
            /** Description */
            description: String? = null,
            /** Category name */
            category: String? = null,
            /** Sub category name */
            subCategory: String? = null,
            /** For function / tool execution */
            toolRegistry: ToolRegistry? = null,
            isStateful: Boolean = false,
        ): SpecialisedAgentActivity = BaseSpecialisedAgentActivityImpl(
            UnstructuredBaseAgent.create(
                chat = chat,
                promptTemplate = promptTemplate,
                input = input,
                id = id,
                description = description,
                category = category,
                subCategory = subCategory,
                toolRegistry = toolRegistry,
                isStateful = isStateful,
            )
        )
    }
}