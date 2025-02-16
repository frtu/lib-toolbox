package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity

import com.github.frtu.kotlin.ai.os.instruction.PromptTemplate
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.UnstructuredBaseAgent
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class BaseSpecialisedAgentActivityImplTest {
    @Test
    fun `Use BaseSpecialisedAgentActivityImpl#create() for PromptTemplate`(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val prompt = PromptTemplate(
            id = "prompt-generation-agent",
            description = "A meta-prompt Agent instructs the model to create a good prompt based on your task description or improve an existing one",
            template = """
                You're a prompt engineer, write a very bespoke, detailed and succinct prompt, that will generate an Cartoon storyboard writer optimized to write a story for my 2 pages cartoon content called UFO of a cute space cat and a police dog chasing him.
                
                Instructions
                - output the prompt you generate in markdown using variables in double curly brackets
                - output the prompt in a codeblock
                """.trimIndent(),
        )
        val chat = mockk<Chat>()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = BaseSpecialisedAgentActivityImpl.create(chat, prompt) as BaseSpecialisedAgentActivityImpl
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            getId() shouldBe prompt.id
            getDescription() shouldBe prompt.description
            getInstructions() shouldBe prompt.template
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}