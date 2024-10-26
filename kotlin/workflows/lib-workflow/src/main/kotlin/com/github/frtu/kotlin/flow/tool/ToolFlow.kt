package com.github.frtu.kotlin.flow.tool

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.frtu.kotlin.flow.core.AbstractFlow
import com.github.frtu.kotlin.llm.os.tool.Tool
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen
import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import com.github.frtu.kotlin.utils.io.toObject

/**
 * Encapsulate lightweight Flow as `Tool`
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
abstract class ToolFlow<INPUT, OUTPUT>(
    /** Name of the tool */
    override val name: String,
    /** Description that can be used by agent to decide which tool to use */
    override val description: String,

    /** Input parameter */
    val parameterClass: Class<INPUT>,
    /** Parameter schema (recommend to only have one parameter) */
    override val parameterJsonSchema: String,

    /** Return type */
    val returnClass: Class<OUTPUT>?,
    /** Return schema. `null` schema when returning `void` */
    override val returnJsonSchema: String? = null,
) : AbstractFlow<INPUT, OUTPUT>(name), Tool {
    constructor(
        name: String,
        parameterClass: Class<INPUT>,
        returnClass: Class<OUTPUT>?,
        description: String? = null,
    ) : this(
        name = name,
        description = description ?: "Business flow:$name",
        parameterClass = parameterClass,
        parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
        returnClass = returnClass,
        returnJsonSchema = returnClass?.let { SchemaGen.generateJsonSchema(returnClass) },
    )

    override suspend fun execute(parameter: JsonNode): JsonNode {
        val event = parameter.toObject(parameterClass)
        val result = this.execute(event)
        return when(returnClass) {
            String::class -> TextNode(result as String)
            null -> NullNode.instance
            else -> {
                result?.toJsonString()?.toJsonNode() ?: NullNode.instance
            }
        }
    }
}