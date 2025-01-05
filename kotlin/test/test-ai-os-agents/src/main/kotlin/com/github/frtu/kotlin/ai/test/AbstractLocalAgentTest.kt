package com.github.frtu.kotlin.ai.test

import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.agent.StructuredBaseAgent
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

abstract class AbstractLocalAgentTest<INPUT, OUTPUT>(
    private val builder: (Chat) -> StructuredBaseAgent<INPUT, OUTPUT>,
    vararg model: String
) {
    private val chatList: List<Chat> = model.map {
        ChatApiConfigs().chatOllama(model = it)
    }

    protected fun benchmarkAcrossModel(
        input: INPUT,
        validator: (OUTPUT) -> Unit
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
                validator(result)
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}