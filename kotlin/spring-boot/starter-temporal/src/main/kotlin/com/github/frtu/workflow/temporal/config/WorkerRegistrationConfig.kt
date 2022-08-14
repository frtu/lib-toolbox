package com.github.frtu.workflow.temporal.config

import com.github.frtu.logs.core.RpcLogger.flowId
import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.message
import com.github.frtu.workflow.temporal.bootstrap.ANNOTATION_FOR_ACTIVITY
import com.github.frtu.workflow.temporal.bootstrap.TemporalConnectivity.workerRegistration
import io.temporal.worker.WorkerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.event.*
import org.springframework.stereotype.Component

@Component
class WorkerRegistrationConfig(private val applicationContext: ApplicationContext) {
    @EventListener(ContextRefreshedEvent::class)
    fun handleContextRefresh() {
        structuredLogger.info(message("context refreshed event received"))
        val factory = applicationContext.getBean(WorkerFactory::class.java)
        val beansWithAnnotation = applicationContext.getBeansWithAnnotation(ANNOTATION_FOR_ACTIVITY)
        workerRegistration(factory,
            if (beansWithAnnotation.isNotEmpty()) {
                beansWithAnnotation.entries.forEach { (beanName, bean) ->
                    structuredLogger.debug(flowId(beanName), message("Found activity bean name[$beanName] class[${bean.javaClass}]"))
                }
                beansWithAnnotation.values.toList()
            } else emptyList()
        )
    }

    @EventListener(ContextClosedEvent::class)
    fun handleContextClosed() {
        structuredLogger.info(message("context closed event received"))
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}