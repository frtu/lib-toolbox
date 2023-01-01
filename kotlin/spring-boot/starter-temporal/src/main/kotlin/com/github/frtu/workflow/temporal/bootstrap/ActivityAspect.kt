package com.github.frtu.workflow.temporal.bootstrap

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.workflow.temporal.annotation.ActivityImplementation
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.stereotype.Component

/**
 * Aspect for any Temporal activities
 * @author fred
 * @since 1.2.4
 */
@Aspect
@Component
@ConditionalOnClass(ActivityImplementation::class)
class ActivityAspect {
    @Around("execution(public * *(..))")
    @Throws(Throwable::class)
    fun proxyMethod(joinPoint: ProceedingJoinPoint): Any? {
        val joinPointSignature = joinPoint.signature
        structuredLogger.debug("Before $joinPointSignature")
        val result = joinPoint.proceed()
        structuredLogger.debug("After $joinPointSignature")
        return result
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}
