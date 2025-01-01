package com.github.frtu.kotlin.ai.os.llm.agent

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.serdes.json.ext.objToJsonNode
import com.github.frtu.kotlin.serdes.json.ext.toJsonObj
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen
import com.github.frtu.kotlin.tool.StructuredTool

/**
 * An agent receiving & returning Structured object
 *
 * @author Frédéric TU
 * @since 2.0.13
 */
abstract class StructuredBaseAgent<INPUT, OUTPUT>(
    /** Id of the agent */
    id: String,
    /** Description */
    description: String,
    /** Input parameter schema */
    val parameterClass: Class<INPUT>,
    /** Return schema. `null` schema when returning `void` */
    val returnClass: Class<OUTPUT>,
    /** Category name */
    category: String? = null,
    /** Sub category name */
    subCategory: String? = null,
    /** System instruction prompt */
    instructions: String? = null,
    /** Engine containing model version */
    chat: Chat,
    /** If Agent should keep conversation across Q&A */
    isStateful: Boolean = false,
) : AbstractAgent(
    id = ActionId(id),
    description = description,
    parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
    returnJsonSchema = SchemaGen.generateJsonSchema(returnClass),
    category = category,
    subCategory = subCategory,
    instructions = instructions,
    chat = chat,
    isStateful = isStateful,
), StructuredTool<INPUT, OUTPUT> {
    
    override suspend fun execute(parameter: INPUT): OUTPUT {
        val request = parameter.toString()
        val response = answer(request).content
        if (response == null) {
            throw IllegalStateException("LLM responded with null value to request:[$request]")
        } else if (!response.trim().startsWith("{")){
            throw IllegalStateException("LLM responded with another format than JSON to request:[$request] response:[$response]")
        } else {
            return response.toJsonObj(returnClass!!)
        }
    }

    override suspend fun execute(parameter: JsonNode): JsonNode {
        val requestObj = parameter.toJsonObj(parameterClass)
        val result = execute(requestObj)
        return result?.objToJsonNode() ?: NullNode.instance
    }
}