package com.github.frtu.kotlin.action.tool.function

import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.ai.os.tool.ToolExecuter
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen

/**
 * Base class for callable function
 */
abstract class Function<INPUT, OUTPUT>(
    id: ActionId,
    description: String,
    parameterJsonSchema: String,
    returnJsonSchema: String?,
//    val parameters: List<Parameter>,
) : ToolExecuter(
    id = id,
    description = description,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
) {
    constructor(
        id: String,
        description: String,
        parameterClass: Class<INPUT>,
        returnClass: Class<OUTPUT>?,
    ) : this(
        id = ActionId(id),
        description = description,
        parameterJsonSchema = SchemaGen.generateJsonSchema(parameterClass),
        returnJsonSchema = returnClass?.let { SchemaGen.generateJsonSchema(returnClass) },
    )
}
