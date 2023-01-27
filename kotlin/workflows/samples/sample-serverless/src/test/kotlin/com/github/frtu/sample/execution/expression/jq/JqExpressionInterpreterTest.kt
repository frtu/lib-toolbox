package com.github.frtu.sample.execution.expression.jq

import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.sample.TestResourceLoader
import com.github.frtu.sample.execution.expression.JsonNodeExpressionInterpreter
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("JqExpressionInterpreter tests")
@ExtendWith(MockKExtension::class)
internal class JqExpressionInterpreterTest {
    // Test subject
    private var expressionInterpreter: JsonNodeExpressionInterpreter = JqExpressionInterpreter()

    @Test
    fun `Positive test cases - evaluateExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ .hello.spanish }"
        val dataNode = TestResourceLoader.loadData().toJsonNode()["greetings"]

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result?.textValue() shouldBe "Hola"
    }

    @Test
    fun `Negative test cases - evaluateExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ .hello.non_existing }"
        val dataNode = TestResourceLoader.loadData().toJsonNode()["greetings"]

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result?.isNull shouldBe true
    }

    @Test
    fun `Positive test cases - evaluateBooleanExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ .hello.spanish == \"Hola\" }"
        val dataNode = TestResourceLoader.loadData().toJsonNode()["greetings"]

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateBooleanExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result shouldBe true
    }

    @Test
    fun `Negative test cases - evaluateBooleanExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ .hello.non_existing == \"Hola\" }"
        val dataNode = TestResourceLoader.loadData().toJsonNode()["greetings"]

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateBooleanExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result shouldBe false
    }

    @Test
    fun `Positive test cases - evaluateArrayExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ .seq }"
        val dataNode = """{ "seq" : [{"key": "first"}, {"key": "second"}] }""".toJsonNode()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateArrayExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result[0]["key"].textValue() shouldBe "first"
    }

    @Test
    fun `Negative test cases - evaluateArrayExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ .non_existing }"
        val dataNode = """{ "seq" : [{"key": "first"}, {"key": "second"}] }""".toJsonNode()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateArrayExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.isEmpty() shouldBe true
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}