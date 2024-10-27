package com.github.frtu.kotlin.llm.os.tool

import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen

/**
 * StructuredToolExecuter is a `ToolExecuter` for a strongly typed Input & Output class
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
abstract class StructuredToolExecuter<INPUT, OUTPUT>(
    /** Name of the tool */
    name: String,
    /** Description that can be used by agent to decide which tool to use */
    description: String,
    /** Input parameter schema */
    private val parameterClass: Class<INPUT>,
    /** Return schema. `null` schema when returning `void` */
    private val returnClass: Class<OUTPUT>?,
) : ToolExecuter(
    name = name,
    description = description,
    parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
    returnJsonSchema = returnClass?.let { SchemaGen.generateJsonSchema(returnClass) },
), GenericAction {
}