package com.github.frtu.sample.execution.expression

import com.fasterxml.jackson.databind.JsonNode

/**
 * An ExpressionInterpreter that uses JsonNode as the exchange object
 *
 * @author frtu
 */
interface JsonNodeExpressionInterpreter : ExpressionInterpreter<JsonNode>