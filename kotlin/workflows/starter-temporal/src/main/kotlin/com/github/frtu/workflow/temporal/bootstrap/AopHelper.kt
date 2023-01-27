package com.github.frtu.workflow.temporal.bootstrap

import com.github.frtu.logs.core.StructuredLogger
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory

/**
 * Helper for AOP that pass all aspect classes and wrap it into any targetObject
 * @author fred
 * @since 1.2.4
 */
class AopHelper(
    private vararg val aspectClasses: Class<*>,
) {
    fun wrap(targetObject: Any): Any {
        val factory = AspectJProxyFactory(targetObject)
        aspectClasses.forEach { aspectClass ->
            factory.addAspect(aspectClass)
        }
        structuredLogger.info("Proxing ${targetObject.javaClass} with aspects : ${aspectClasses.joinToString { it.canonicalName }}")
        return factory.getProxy<Any>()
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}