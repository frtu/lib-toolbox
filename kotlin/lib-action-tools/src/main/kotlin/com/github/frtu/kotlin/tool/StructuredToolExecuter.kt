package com.github.frtu.kotlin.tool

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen

/**
 * StructuredToolExecuter is a `ToolExecuter` for a strongly typed Input & Output class
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
abstract class StructuredToolExecuter<INPUT, OUTPUT>(
    /** Id of the tool */
    id: ActionId,
    /** Description that can be used by agent to decide which tool to use */
    description: String,
    /** Input parameter schema */
    val parameterClass: Class<INPUT>,
    /** Return schema. `null` schema when returning `void` */
    val returnClass: Class<OUTPUT>?,
) : ToolExecuter(
    id = id,
    description = description,
    parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
    returnJsonSchema = returnClass?.let { SchemaGen.generateJsonSchema(returnClass) },
), GenericAction {
    constructor(
        id: String,
        description: String,
        parameterClass: Class<INPUT>,
        returnClass: Class<OUTPUT>?,
    ) : this(
        id = ActionId(id),
        description = description,
        parameterClass = parameterClass,
        returnClass = returnClass,
    )

    companion object {
        /**
         * Allow to create a StructuredToolExecuter by passing only the execution
         * @since 2.0.9
         */
        fun <INPUT, OUTPUT> create(
            id: String,
            description: String,
            parameterClass: Class<INPUT>,
            returnClass: Class<OUTPUT>?,
            executer: (JsonNode) -> JsonNode,
        ): Tool = object : StructuredToolExecuter<INPUT, OUTPUT>(
            id = id,
            description = description,
            parameterClass = parameterClass,
            returnClass = returnClass,
        ) {
            override suspend fun execute(parameter: JsonNode): JsonNode = executer.invoke(parameter)
        }
    }
}