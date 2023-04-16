package com.github.frtu.sample.temporal.dynamicwkf.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import net.thisptr.jackson.jq.BuiltinFunctionLoader
import net.thisptr.jackson.jq.JsonQuery
import net.thisptr.jackson.jq.Scope
import net.thisptr.jackson.jq.Versions
import net.thisptr.jackson.jq.module.loaders.BuiltinModuleLoader
import java.util.ArrayList
import java.util.HashMap
import java.util.function.Consumer

class JQFilter private constructor() {
    fun evaluateExpression(expression: String, data: JsonNode?): JsonNode {
        return try {
            val childScope: Scope = Scope.newChildScope(rootScope)
            val result: MutableList<JsonNode> = ArrayList()
            val toEvalExpression = expression.replace("\${", "").replace("}$".toRegex(), "")
            if (!expressionMap.containsKey(toEvalExpression)) {
                expressionMap[toEvalExpression] = JsonQuery.compile(toEvalExpression, Versions.JQ_1_6)
            }
            val jsonQuery = expressionMap[toEvalExpression]!!
            jsonQuery.apply(childScope, data, result::add)
            result[0]
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun evaluateBooleanExpression(expression: String, data: JsonNode?): Boolean {
        val result = evaluateExpression(expression, data) as BooleanNode
        return result.booleanValue()
    }

    fun evaluateArrayExpression(expression: String, data: JsonNode?): List<JsonNode> {
        val result = evaluateExpression(expression, data) as ArrayNode
        val resultList: MutableList<JsonNode> = ArrayList(result.size())
        result.forEach(
            Consumer { jsonNode: JsonNode ->
                resultList.add(
                    jsonNode
                )
            }
        )
        return resultList
    }

    companion object {
        private val rootScope: Scope = Scope.newEmptyScope()
        private val expressionMap: MutableMap<String, JsonQuery> = HashMap<String, JsonQuery>()

        @Volatile
        var instance: JQFilter? = null
            get() {
                if (field == null) {
                    synchronized(JQFilter::class.java) {
                        if (field == null) {
                            field = JQFilter()
                        }
                    }
                }
                return field
            }
            private set
    }

    init {
        BuiltinFunctionLoader.getInstance().loadFunctions(Versions.JQ_1_6, rootScope)
        rootScope.moduleLoader = BuiltinModuleLoader.getInstance()
    }
}
