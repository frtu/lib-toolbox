package com.github.frtu.kotlin.tool

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.action.execution.TypedAction
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.serdes.json.ext.objToJsonNode
import com.github.frtu.kotlin.serdes.json.ext.toJsonObj
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen
import java.lang.reflect.Method

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
    category: String? = null,
    subCategory: String? = null,
) : ToolExecuter(
    id = id,
    description = description,
    parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
    returnJsonSchema = returnClass?.let { SchemaGen.generateJsonSchema(returnClass) },
    category = category,
    subCategory = subCategory,
), StructuredTool<INPUT, OUTPUT> {
    constructor(
        id: String,
        description: String,
        parameterClass: Class<INPUT>,
        returnClass: Class<OUTPUT>?,
        category: String? = null,
        subCategory: String? = null,
    ) : this(
        id = ActionId(id),
        description = description,
        parameterClass = parameterClass,
        returnClass = returnClass,
        category = category,
        subCategory = subCategory,
    )

    override suspend fun execute(parameter: JsonNode): JsonNode {
        val requestObj = parameter.toJsonObj(parameterClass)
        val result = execute(requestObj)
        return result?.objToJsonNode() ?: NullNode.instance
    }

    companion object {
        /**
         * ATTENTION : `executerMethod` - suspend fun are not supported for the moment
         * Will raise `wrong number of arguments: x expected: x+1` that need Continuation arg
         * @since 2.0.9
         */
        fun <INPUT, OUTPUT> create(
            id: String,
            description: String,
            executerMethod: Method,
            targetObject: Any,
            category: String? = null,
            subCategory: String? = null,
        ): StructuredTool<INPUT, OUTPUT> = object : StructuredToolExecuter<INPUT, OUTPUT>(
            id = ActionId(id),
            description = description,
            parameterClass = executerMethod.parameterTypes[0] as Class<INPUT>,
            returnClass = executerMethod.returnType as Class<OUTPUT>,
            category = category,
            subCategory = subCategory,
        ) {
            override suspend fun execute(parameter: INPUT): OUTPUT {
                return executerMethod.invoke(targetObject, parameter) as OUTPUT
            }
        }

        /**
         * Allow to create a StructuredToolExecuter by passing only the execution
         * @since 2.0.9
         */
        fun <INPUT, OUTPUT> create(
            id: String,
            description: String,
            parameterClass: Class<INPUT>,
            returnClass: Class<OUTPUT>?,
            executer: (INPUT) -> OUTPUT,
            category: String? = null,
            subCategory: String? = null,
        ): StructuredTool<INPUT, OUTPUT> = object : StructuredToolExecuter<INPUT, OUTPUT>(
            id = id,
            description = description,
            parameterClass = parameterClass,
            returnClass = returnClass,
            category = category,
            subCategory = subCategory,
        ) {
            override suspend fun execute(parameter: INPUT): OUTPUT = executer.invoke(parameter)
        }
    }
}