package com.github.frtu.kotlin.llm.os.llm.openai

import com.aallam.openai.api.chat.ChatCompletionFunction
import com.aallam.openai.api.chat.Parameters
import com.github.frtu.kotlin.llm.os.tool.Tool

object ToolExt

fun Tool.toChatCompletionFunction() = ChatCompletionFunction(
    id.value, description,
    Parameters.fromJsonString(parameterJsonSchema),
)