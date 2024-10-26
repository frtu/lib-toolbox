package com.github.frtu.kotlin.llm.os.tool.function

import com.aallam.openai.api.chat.ChatCompletionFunction
import com.aallam.openai.api.chat.Parameters
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.frtu.kotlin.llm.os.tool.Tool
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen
import kotlin.reflect.KFunction2

/**
 * Base class for callable function
 */
abstract class Function(
    name: String,
    description: String,
    parameterJsonSchema: String,
    returnJsonSchema: String,
//    val parameters: List<Parameter>,
) : Tool(
    name = name,
    description = description,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
) {
    constructor(
        name: String,
        description: String,
        parameterClass: Class<*>,
        returnClass: Class<*>,
    ) : this(
        name = name,
        description = description,
        parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
        returnJsonSchema = SchemaGen.generateJsonSchema(returnClass),
    )

    fun toChatCompletionFunction() = ChatCompletionFunction(
        name, description,
        Parameters.fromJsonString(parameterJsonSchema),
    )
}
