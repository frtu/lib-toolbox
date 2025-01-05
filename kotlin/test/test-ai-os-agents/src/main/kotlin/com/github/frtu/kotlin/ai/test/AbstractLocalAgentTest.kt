package com.github.frtu.kotlin.ai.test

import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.StructuredBaseAgent
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

abstract class AbstractLocalAgentTest<INPUT, OUTPUT>(
    vararg model: String,
) {
    private val chatList: List<Chat> = model.map {
        ChatApiConfigs().chatOllama(model = it)
    }

    protected fun benchmarkAcrossModel(
        input: INPUT,
        expectedOutput: OUTPUT,
        builder: (Chat) -> StructuredBaseAgent<INPUT, OUTPUT>
    ): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        chatList.map(builder)
            .forEach { agent ->
                //--------------------------------------
                // 2. Execute
                //--------------------------------------
                val result = agent.execute(input)
                logger.debug("result:{}", result)

                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                result.shouldNotBeNull()
                result shouldBe expectedOutput
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}