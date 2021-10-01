package com.github.frtu.kotlin.flow.interceptors

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.*

/**
 * Log interceptor
 * @author frtu
 * @since 1.1.4
 */
class LogInterceptorFlow(
    private val structuredLogger: StructuredLogger,
    private val flowName: String,
) {
    /**
     * @param eventId Unique ID for this event
     * @param flowQualifier additional qualifier for this execution
     */
    fun start(eventId: String, flowQualifier: String = ""): Array<MutableMap.MutableEntry<Any?, Any?>> =
        buildFlowSignature(eventId, flowQualifier).apply {
            structuredLogger.debug(this, phase("START"))
        }

    /**
     * @param eventSignature Signature for this event
     */
    fun end(eventSignature: Array<out MutableMap.MutableEntry<Any?, Any?>>, throwable: Throwable? = null) =
        if (throwable == null) {
            structuredLogger.info(eventSignature, phase("END"))
        } else {
            structuredLogger.error(eventSignature, phase("END_ERROR"), message(throwable.stackTraceToString()))
        }

    /**
     * @param eventId Unique ID for this event
     * @param flowQualifier additional qualifier for this execution
     */
    fun buildFlowSignature(eventId: String, flowQualifier: String = "") =
        entries(flow("$flowName$flowQualifier"), flowId(eventId))!!
}