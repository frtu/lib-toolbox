package com.github.frtu.kotlin.flow.interceptors

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.entries
import com.github.frtu.logs.core.StructuredLogger.flow
import com.github.frtu.logs.core.StructuredLogger.flowId
import com.github.frtu.logs.core.StructuredLogger.phase

/**
 * Log interceptor
 * @author frtu
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
    fun end(eventSignature: Array<out MutableMap.MutableEntry<Any?, Any?>>) {
        structuredLogger.info(eventSignature, phase("END"))
    }

    /**
     * @param eventId Unique ID for this event
     * @param flowQualifier additional qualifier for this execution
     */
    fun buildFlowSignature(eventId: String, flowQualifier: String = "") =
        entries(flow("$flowName$flowQualifier"), flowId(eventId))!!
}