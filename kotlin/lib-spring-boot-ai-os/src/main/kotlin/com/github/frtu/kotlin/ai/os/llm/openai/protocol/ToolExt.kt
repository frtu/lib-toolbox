package com.github.frtu.kotlin.ai.os.llm.openai.protocol

import com.aallam.openai.api.chat.ChatCompletionFunction
import com.aallam.openai.api.chat.Parameters
import com.github.frtu.kotlin.tool.Tool

object ToolExt

fun Tool.toChatCompletionFunction() = ChatCompletionFunction(
    id.value, description,
    Parameters.fromJsonString(parameterJsonSchema),
)