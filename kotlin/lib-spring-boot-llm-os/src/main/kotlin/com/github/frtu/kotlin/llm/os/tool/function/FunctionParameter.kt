package com.github.frtu.kotlin.llm.os.tool.function

data class FunctionParameter(
    val name: String,
    val description: String? = null,
    val type: String,
    val schema: String, // Need more structure
)