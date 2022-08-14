package com.github.frtu.workflow.temporal.bootstrap

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.message
import com.github.frtu.workflow.temporal.annotation.ActivityImplementation
import io.temporal.worker.WorkerFactory

object TemporalConnectivity {
    fun workerRegistration(factory: WorkerFactory, activities: List<Any>) {
        structuredLogger.info(message("Starting factory with activities[${activities.size}]"))
        activities.map { factory.newWorker(getTaskQueue(it)).registerActivitiesImplementations(it) }
        // Start listening to the Task Queue.
        factory.start()
    }

    private fun getTaskQueue(obj: Any): String = getAnnotation(obj, ANNOTATION_FOR_ACTIVITY).taskQueue

    private fun <A : Annotation> getAnnotation(obj: Any, annotation: Class<A>): A =
        obj.javaClass.getAnnotation(annotation)

    private val structuredLogger = StructuredLogger.create(this::class.java)
}

val ANNOTATION_FOR_ACTIVITY = ActivityImplementation::class.java
