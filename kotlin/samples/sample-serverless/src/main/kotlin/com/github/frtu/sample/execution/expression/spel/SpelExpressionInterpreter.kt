package com.github.frtu.sample.execution.expression.spel

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.patterns.AbstractRegistry
import com.github.frtu.kotlin.patterns.UnrecognizedElementException
import com.github.frtu.sample.execution.expression.JsonNodeExpressionInterpreter
import org.springframework.expression.Expression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.SpelEvaluationException
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.integration.json.JsonPropertyAccessor

/**
 * An ExpressionInterpreter implemented by Spring Expression Language.
 *
 * @author frtu
 */
class SpelExpressionInterpreter(
    private val expressionParser: ExpressionParser = SpelExpressionParser(),
    registry: MutableMap<String, Expression> = mutableMapOf(),
) : JsonNodeExpressionInterpreter, AbstractRegistry<String, Expression>("spel-expression", registry) {
    /**
     * Evaluate an expression returning a Boolean
     */
    override fun evaluateBooleanExpression(expression: String, data: JsonNode): Boolean? = try {
        val (expression, context) = evaluate(expression, data)
        expression.getValue(context, Boolean::class.java).apply {
            logger.debug("Execute expression:$expression return:$this")
        } ?: false
    } catch (e: SpelEvaluationException) {
        logger.debug("Execute expression:$expression return:null")
        null
    }

    /**
     * Evaluate an expression returning an object
     */
    override fun evaluateExpression(expression: String, data: JsonNode): JsonNode? = try {
        val (expression, context) = evaluate(expression, data)
        val result = expression.getValue(context) as JsonPropertyAccessor.ToStringFriendlyJsonNode
        logger.debug("Execute expression:$expression return:$result type:${result?.javaClass}")
        result.target
    } catch (e: SpelEvaluationException) {
        logger.debug("Execute expression:$expression return:null")
        null
    }

    /**
     * Evaluate an expression returning a list of objects
     */
    override fun evaluateArrayExpression(expression: String, data: JsonNode): List<JsonNode> {
        TODO("Not yet implemented")
    }

    private fun evaluate(
        expression: String,
        data: JsonNode
    ): Pair<Expression, StandardEvaluationContext> {
        val toEvalExpression = expression.replace("\${", "").replace("}$".toRegex(), "")
        val expression = try {
            this[toEvalExpression]
        } catch (e: UnrecognizedElementException) {
            expressionParser.parseExpression(toEvalExpression).apply {
                register(toEvalExpression, this)
            }
        }
        val context = StandardEvaluationContext(data).apply {
            this.propertyAccessors.add(JsonPropertyAccessor())
        }
        return Pair(expression, context)
    }
}
