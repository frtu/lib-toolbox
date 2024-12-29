package com.github.frtu.kotlin.tool

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen
import com.github.frtu.kotlin.tool.Tool
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * ToolExecuter the default implementation and constructor for Tool
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
abstract class ToolExecuter(
    /** Id of the tool */
    override val id: ActionId,
    /** Description that can be used by agent to decide which tool to use */
    override val description: String,
    /** Input parameter schema (recommend to only have one parameter) */
    override val parameterJsonSchema: String,
    /** Return schema. `null` schema when returning `void` */
    override val returnJsonSchema: String? = null,

    /** Category name */
    override val category: String? = null,
    /** Sub category name */
    override val subCategory: String? = null,
) : Tool {
    constructor(
        id: String,
        description: String,
        parameterClass: Class<*>,
        returnClass: Class<*>?,
        category: String? = null,
        subCategory: String? = null,
    ) : this(
        id = ActionId(id),
        description = description,
        parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
        returnJsonSchema = returnClass?.let { SchemaGen.generateJsonSchema(returnClass) },
        category = category,
        subCategory = subCategory,
    )

    companion object {
        fun create(
            id: String,
            description: String,
            parameterJsonSchema: String,
            returnJsonSchema: String? = null,
            executer: (JsonNode) -> JsonNode,
            category: String? = null,
            subCategory: String? = null,
        ): Tool = object : Tool {
            override val id = ActionId(id)
            override val description = description
            override val parameterJsonSchema = parameterJsonSchema
            override val returnJsonSchema = returnJsonSchema
            override val category: String? = null
            override val subCategory: String? = null
            override suspend fun execute(parameter: JsonNode): JsonNode = executer.invoke(parameter)
        }
    }

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
}