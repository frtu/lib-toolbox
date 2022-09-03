package com.github.frtu.sample.execution.expression

import com.fasterxml.jackson.databind.JsonNode

interface ExpressionInterpreter {
    fun evaluateExpression(expression: String, data: JsonNode): JsonNode
    fun evaluateBooleanExpression(expression: String, data: JsonNode): Boolean
    fun evaluateArrayExpression(expression: String, data: JsonNode): List<JsonNode>
}