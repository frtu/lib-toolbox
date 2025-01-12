package com.github.frtu.kotlin.ai.spring.builder

import com.github.frtu.kotlin.ai.os.instruction.PromptTemplate
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.UnstructuredBaseAgent
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

/**
 * Allow to auto create Agent (UnstructuredBaseAgent : String as INPUT and OUTPUT) from a PromptTemplate.
 *
 * ATTENTION : PromptTemplate with variables are NOT supported and need to be initialized by creating UnstructuredBaseAgent manually.
 * @since 2.0.14
 */
class AgentFromPromptConfig {
    @Bean
    fun agentsGeneratedByPrompts(
        chat: Chat,
        prompts: List<PromptTemplate>,
    ): List<UnstructuredBaseAgent> = prompts.map { prompt ->
        UnstructuredBaseAgent.create(chat, prompt).also { agent ->
            logger.info("Created new agent id:${agent.id} from prompt:{}", prompt)
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}