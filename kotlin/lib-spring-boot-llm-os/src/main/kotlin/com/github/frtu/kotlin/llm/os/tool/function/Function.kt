package com.github.frtu.kotlin.llm.os.tool.function

import com.aallam.openai.api.chat.ChatCompletionFunction
import com.aallam.openai.api.chat.Parameters
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen
import kotlin.reflect.KFunction2

/**
 * Base class for callable function
 */
data class Function(
    val name: String,
    val description: String? = null,
    val action: KFunction2<String, String, String>,
    val parameterJsonSchema: String,
    val returnJsonSchema: String,
//    val parameters: List<Parameter>,
) {
    fun toChatCompletionFunction() = ChatCompletionFunction(
        name, description,
        Parameters.fromJsonString(parameterJsonSchema),
    )
}

fun function(
    name: String,
    description: String? = null,
    action: KFunction2<String, String, String>,
    parameterJsonSchema: String,
    returnJsonSchema: String,
) = Function(name, description, action, parameterJsonSchema, returnJsonSchema)

fun function(
    name: String,
    description: String? = null,
    action: KFunction2<String, String, String>,
    parameterClass: Class<*>,
    returnClass: Class<*>,
) = Function(
    name,
    description,
    action,
    SchemaGen.generateJsonSchema(parameterClass),
    SchemaGen.generateJsonSchema(returnClass),
)