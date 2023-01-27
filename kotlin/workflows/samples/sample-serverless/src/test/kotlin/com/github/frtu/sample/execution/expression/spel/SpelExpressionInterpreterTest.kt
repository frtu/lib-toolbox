package com.github.frtu.sample.execution.expression.spel

import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.sample.TestResourceLoader
import com.github.frtu.sample.execution.expression.JsonNodeExpressionInterpreter
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("SpelExpressionInterpreter tests")
@ExtendWith(MockKExtension::class)
internal class SpelExpressionInterpreterTest {
    // Test subject
    private var expressionInterpreter: JsonNodeExpressionInterpreter = SpelExpressionInterpreter()

    @Test
    fun `Positive test cases - evaluateExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ hello.spanish }"
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
        val expression = "\${ hello.non_existing }"
        val dataNode = TestResourceLoader.loadData().toJsonNode()["greetings"]

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.shouldBeNull()
    }

    @Test
    fun `Positive test cases - evaluateBooleanExpression`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val expression = "\${ hello.spanish.toString() == 'Hola' }"
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
        val expression = "\${ hello.non_existing == \"Hola\" }"
        val dataNode = TestResourceLoader.loadData().toJsonNode()["greetings"]

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = expressionInterpreter.evaluateBooleanExpression(expression, dataNode)
        logger.debug("expression:{} input:{} => result:{}", expression, dataNode, result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        result.shouldBeNull()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}