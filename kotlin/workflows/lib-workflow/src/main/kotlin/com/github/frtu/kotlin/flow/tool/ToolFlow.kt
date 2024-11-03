package com.github.frtu.kotlin.flow.tool

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.action.tool.Tool
import com.github.frtu.kotlin.flow.core.AbstractFlow
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen
import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import com.github.frtu.kotlin.utils.io.toObject

/**
 * Encapsulate lightweight Flow as `Tool`
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
abstract class ToolFlow<INPUT, OUTPUT>(
    /** Name of the tool */
    override val id: ActionId,
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
) : AbstractFlow<INPUT, OUTPUT>(id.value), Tool {
    constructor(
        id: String,
        parameterClass: Class<INPUT>,
        returnClass: Class<OUTPUT>?,
        description: String? = null,
    ) : this(
        id = ActionId(id),
        description = description ?: "Business flow:$id",
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