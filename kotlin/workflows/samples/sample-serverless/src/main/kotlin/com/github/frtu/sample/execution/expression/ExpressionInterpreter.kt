package com.github.frtu.sample.execution.expression

/**
 * Interpreter allowing to evaluate an expression & returning boolean or object
 *
 * @author frtu
 * @param <T> a structure representing an object
 */
interface ExpressionInterpreter<T> {
    /**
     * Evaluate an expression returning a Boolean
     */
    fun evaluateBooleanExpression(expression: String, data: T): Boolean?

    /**
     * Evaluate an expression returning an object
     */
    fun evaluateExpression(expression: String, data: T): T?

    /**
     * Evaluate an expression returning a list of objects
     */
    fun evaluateArrayExpression(expression: String, data: T): List<T>
}