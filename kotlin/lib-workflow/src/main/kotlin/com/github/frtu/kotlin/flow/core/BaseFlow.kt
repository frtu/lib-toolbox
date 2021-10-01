package com.github.frtu.kotlin.flow.core

import com.github.frtu.kotlin.flow.interceptors.LogInterceptorFlow
import com.github.frtu.logs.core.StructuredLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Prototype for a flow
 * @author frtu
 * @since 1.1.4
 */
abstract class BaseFlow<INPUT, OUTPUT>(
    val flowName: String,
    private val logger: Logger = LoggerFactory.getLogger("flow.$flowName"),
) {
    fun execute(input: INPUT): OUTPUT {
        val eventSignature = beforeExecution(input)

        // Precondition
        doValidation(input)

        // Execution
        val output = doExecute(input)

        // End framework
        afterExecution(eventSignature)

        return output
    }

    abstract fun extractId(input: INPUT): String
    abstract fun doValidation(input: INPUT)
    abstract fun doExecute(input: INPUT): OUTPUT

    internal val structuredLogger = StructuredLogger.create(logger)
    internal val logInterceptorFlow: LogInterceptorFlow = LogInterceptorFlow(structuredLogger, flowName)

    // TODO Move to a dedicated Util class
    protected fun checkNotNull(paramValue: Any?, paramName: String) =
        paramValue ?: throw IllegalArgumentException("$paramName should NOT be null")

    // TODO Make interceptor more generic
    private fun beforeExecution(event: INPUT): Array<MutableMap.MutableEntry<Any?, Any?>> =
        logInterceptorFlow.start(extractId(event))

    private fun afterExecution(eventSignature: Array<MutableMap.MutableEntry<Any?, Any?>>) =
        logInterceptorFlow.end(eventSignature)
}