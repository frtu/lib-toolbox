package com.github.frtu.kotlin.llm.os.llm.openai

import com.aallam.openai.api.chat.ChatCompletionFunction
import com.aallam.openai.api.chat.Parameters
import com.github.frtu.kotlin.llm.os.tool.Tool
import com.github.frtu.kotlin.llm.os.tool.function.Function

object ToolExt

fun Tool.toChatCompletionFunction() = ChatCompletionFunction(
    name, description,
    Parameters.fromJsonString(parameterJsonSchema),
)

fun Function.toChatCompletionFunction() = ChatCompletionFunction(
    name, description,
    Parameters.fromJsonString(parameterJsonSchema),
)