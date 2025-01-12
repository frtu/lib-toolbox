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
    private val chatList: List<Pair<String, Chat>> = model.map { model ->
        Pair(model, ChatApiConfigs().chatOllama(model = model))
    }

    protected fun benchmarkAcrossModel(
        input: INPUT,
        validator: (String, OUTPUT) -> Unit
    ): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        chatList
            .map { (model, chat) ->
                Pair(model, builder(chat))
            }
            .forEach { (model, agent) ->
                //--------------------------------------
                // 2. Execute
                //--------------------------------------
                val output = agent.execute(input)
                logger.debug("Received from model:[{}] with output:{}", model, output)

                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                output.shouldNotBeNull()
                // Can create different condition check depending on model
                validator(model, output)
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}