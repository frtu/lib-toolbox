package com.github.frtu.workflow.temporal.bootstrap

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.tracing.annotation.ExecutionSpanAspect
import com.github.frtu.workflow.temporal.annotation.ActivityImplementation
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

/**
 * Aspect for any Temporal activities
 * @author fred
 * @since 1.2.4
 */
@Aspect
@Component
@ConditionalOnClass(ActivityImplementation::class, ExecutionSpanAspect::class)
class ActivityAspect(
    private val executionSpanAspect: ExecutionSpanAspect = ExecutionSpanAspect(),
) {
    @Around("execution(public * *(..))")
    @Throws(Throwable::class)
    fun proxyMethod(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = Instant.now().toEpochMilli()

        val operationName: String? = extractOperationName(joinPoint.signature)
        structuredLogger.debug("Activity start : $operationName")

        return try {
            joinPoint.proceed()
        } catch (e: Exception) {
            structuredLogger.error(e, "Activity end : $operationName took ${Instant.now().toEpochMilli() - startTime} ms")
            throw e
        } finally {
            structuredLogger.debug("Activity end : $operationName took ${Instant.now().toEpochMilli() - startTime} ms")
        }
    }

    private fun extractOperationName(joinPointSignature: Signature): String? {
        val operationName: String? = if (joinPointSignature is MethodSignature) {
            joinPointSignature.method.name
        } else {
            "${joinPointSignature.declaringType}.${joinPointSignature.name}"
        }
        return operationName
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}
