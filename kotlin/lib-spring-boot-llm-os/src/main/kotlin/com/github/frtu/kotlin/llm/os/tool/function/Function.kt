package com.github.frtu.kotlin.llm.os.tool.function

import com.github.frtu.kotlin.llm.os.tool.ToolExecuter
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen

/**
 * Base class for callable function
 */
abstract class Function(
    name: String,
    description: String,
    parameterJsonSchema: String,
    returnJsonSchema: String?,
//    val parameters: List<Parameter>,
) : ToolExecuter(
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
}
