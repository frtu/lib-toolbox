package com.github.frtu.sample.execution.expression.jq

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.patterns.AbstractRegistry
import com.github.frtu.kotlin.patterns.UnrecognizedElementException
import com.github.frtu.sample.execution.expression.JsonNodeExpressionInterpreter
import net.thisptr.jackson.jq.*
import net.thisptr.jackson.jq.module.loaders.BuiltinModuleLoader
import java.util.*
import java.util.function.Consumer

class JqExpressionInterpreter(
    private val rootScope: Scope = Scope.newEmptyScope(),
    jqVersion: Version = Versions.JQ_1_6,
    registry: MutableMap<String, JsonQuery> = mutableMapOf(),
) : JsonNodeExpressionInterpreter, AbstractRegistry<String, JsonQuery>("jq-expression", registry) {

    init {
        BuiltinFunctionLoader.getInstance().loadFunctions(jqVersion, rootScope)
        rootScope.moduleLoader = BuiltinModuleLoader.getInstance()
    }

    override fun evaluateExpression(expression: String, data: JsonNode): JsonNode {
        return try {
            val childScope: Scope = Scope.newChildScope(rootScope)
            val result: MutableList<JsonNode> = ArrayList()
            val toEvalExpression = expression.replace("\${", "").replace("}$".toRegex(), "")
            val jsonQuery = try {
                this[toEvalExpression]
            } catch (e: UnrecognizedElementException) {
                JsonQuery.compile(toEvalExpression, Versions.JQ_1_6).apply {
                    register(toEvalExpression, this)
                }
            }
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
