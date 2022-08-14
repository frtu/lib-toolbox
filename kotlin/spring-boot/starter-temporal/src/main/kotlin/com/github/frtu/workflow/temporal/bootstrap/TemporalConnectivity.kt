package com.github.frtu.workflow.temporal.bootstrap

import com.github.frtu.logs.core.RpcLogger.responseBody
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.message
import com.github.frtu.workflow.temporal.annotation.ActivityImplementation
import com.github.frtu.workflow.temporal.annotation.WorkflowImplementation
import io.temporal.worker.WorkerFactory
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.springframework.aop.framework.Advised
import org.springframework.aop.support.AopUtils

class TemporalConnectivity(private val basePackage: String = "") {
    fun workerRegistration(factory: WorkerFactory, activities: List<Any>) {
        structuredLogger.info(message("Starting factory with activities[${activities.size}]"))
        activities.map {
            val activityTaskQueue = getActivityTaskQueue(it)
            factory.newWorker(activityTaskQueue).registerActivitiesImplementations(it)
        }
        scanClasspathForWorkflow().map {
            val workflowTaskQueue = getWorkflowTaskQueue(it)
            factory.newWorker(workflowTaskQueue).registerWorkflowImplementationTypes(it)
        }
        // Start listening to the Task Queue.
        factory.start()
    }

    internal fun scanClasspathForWorkflow(): Set<Class<*>> {
        val reflections = Reflections(basePackage, SubTypesScanner(), TypeAnnotationsScanner())
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(ANNOTATION_FOR_WORKFLOW)
        structuredLogger.info(
            message("Scanned basePackage[$basePackage] for @$ANNOTATION_FOR_WORKFLOW"), responseBody(typesAnnotatedWith)
        )
        return typesAnnotatedWith ?: emptySet()
    }

    internal fun getWorkflowTaskQueue(javaClass: Class<*>): String =
        javaClass.getAnnotation(ANNOTATION_FOR_WORKFLOW).taskQueue

    private fun getActivityTaskQueue(bean: Any): String = getAnnotation(bean, ANNOTATION_FOR_ACTIVITY).taskQueue

    private fun <A : Annotation> getAnnotation(bean: Any, annotation: Class<A>): A {
        val obj = if (AopUtils.isAopProxy(bean) && bean is Advised) {
            bean.targetSource.target!!
        } else bean
        return obj.javaClass.getAnnotation(annotation)
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}

val ANNOTATION_FOR_ACTIVITY = ActivityImplementation::class.java
val ANNOTATION_FOR_WORKFLOW = WorkflowImplementation::class.java
