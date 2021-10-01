package com.github.frtu.kotlin.flow.core

import com.github.frtu.kotlin.flow.interceptors.LogInterceptorFlow
import com.github.frtu.logs.core.StructuredLogger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Prototype for a flow
 * @author frtu
 */
abstract class BaseFlow<T>(
    flowName: String,
) {
    fun execute(input: T) {
        val eventSignature = beforeExecution(input)

        // Precondition
        doValidation(input)

        // Execution
        doExecute(input)

        // End framework
        afterExecution(eventSignature)
    }

    abstract fun extractId(input: T): String
    abstract fun doValidation(input: T)
    abstract fun doExecute(input: T)

    internal val structuredLogger = StructuredLogger.create(LoggerFactory.getLogger("flow.$flowName"))
    internal val logInterceptorFlow: LogInterceptorFlow = LogInterceptorFlow(structuredLogger, flowName)

    // TODO Move to a dedicated Util class
    protected fun checkNotNull(paramValue: UUID?, paramName: String) =
        paramValue ?: throw IllegalArgumentException("$paramName should NOT be null")

    // TODO Make interceptor more generic
    private fun beforeExecution(input: T): Array<MutableMap.MutableEntry<Any?, Any?>> =
        logInterceptorFlow.start(extractId(input))

    private fun afterExecution(eventSignature: Array<MutableMap.MutableEntry<Any?, Any?>>) =
        logInterceptorFlow.end(eventSignature)
}