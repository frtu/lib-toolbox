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
class Function(
    name: String,
    description: String,
    parameterJsonSchema: String,
    returnJsonSchema: String,
    val action: KFunction2<String, String, String>,
//    val parameters: List<Parameter>,
) : Tool(
    name = name,
    description = description,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
) {
    override suspend fun execute(parameter: JsonNode): JsonNode {
        val location = parameter["location"].textValue()
        val unit = parameter["unit"]?.textValue() ?: "fahrenheit"
        val result = action.invoke(location, unit)
        return TextNode.valueOf(result)
    }

    fun toChatCompletionFunction() = ChatCompletionFunction(
        name, description,
        Parameters.fromJsonString(parameterJsonSchema),
    )
}

fun function(
    name: String,
    description: String,
    action: KFunction2<String, String, String>,
    parameterJsonSchema: String,
    returnJsonSchema: String,
) = Function(name, description, parameterJsonSchema, returnJsonSchema, action)

fun function(
    name: String,
    description: String,
    action: KFunction2<String, String, String>,
    parameterClass: Class<*>,
    returnClass: Class<*>,
) = Function(
    name,
    description,
    SchemaGen.generateJsonSchema(parameterClass),
    SchemaGen.generateJsonSchema(returnClass),
    action,
)