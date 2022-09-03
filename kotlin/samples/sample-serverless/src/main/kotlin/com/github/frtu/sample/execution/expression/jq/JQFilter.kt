package com.github.frtu.sample.execution.expression.jq

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.sample.execution.expression.ExpressionInterpreter
import net.thisptr.jackson.jq.*
import net.thisptr.jackson.jq.module.loaders.BuiltinModuleLoader
import java.util.*
import java.util.function.Consumer

class JQFilter(
    private val rootScope: Scope = Scope.newEmptyScope(),
    jqVersion: Version = Versions.JQ_1_6,
    private val expressionCache: MutableMap<String, JsonQuery> = HashMap<String, JsonQuery>(),
) : ExpressionInterpreter {

    init {
        BuiltinFunctionLoader.getInstance().loadFunctions(jqVersion, rootScope)
        rootScope.moduleLoader = BuiltinModuleLoader.getInstance()
    }

    override fun evaluateExpression(expression: String, data: JsonNode): JsonNode {
        return try {
            val childScope: Scope = Scope.newChildScope(rootScope)
            val result: MutableList<JsonNode> = ArrayList()
            val toEvalExpression = expression.replace("\${", "").replace("}$".toRegex(), "")
            if (!expressionCache.containsKey(toEvalExpression)) {
                expressionCache[toEvalExpression] =
                    JsonQuery.compile(toEvalExpression, Versions.JQ_1_6)
            }
            val jsonQuery = expressionCache[toEvalExpression]!!
            jsonQuery.apply(childScope, data, result::add)
            result[0]
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun evaluateBooleanExpression(expression: String, data: JsonNode): Boolean {
        val result = evaluateExpression(expression, data) as BooleanNode
        return result.booleanValue()
    }

    override fun evaluateArrayExpression(expression: String, data: JsonNode): List<JsonNode> {
        return when (val result = evaluateExpression(expression, data)) {
            is ArrayNode -> {
                val resultList: MutableList<JsonNode> = ArrayList(result.size())
                result.forEach(Consumer { jsonNode: JsonNode ->
                    resultList.add(
                        jsonNode
                    )
                })
                resultList
            }
            is NullNode -> emptyList()
            else -> {
                throw IllegalStateException("Unrecognized result:$result")
            }
        }
    }
}
