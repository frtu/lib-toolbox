package com.github.frtu.sample.temporal.config

import com.github.frtu.sample.temporal.staticwkf.workflow.SubscriptionWorkflowImpl
import com.github.frtu.sample.temporal.staticwkf.workflow.TASK_QUEUE_SUBSCRIPTION
import io.temporal.worker.WorkerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkflowSubscriptionConfig {
    /**
     * There should ONLY have one worker per application
     */
    @Bean
    fun worker(factory: WorkerFactory): String {
        factory.newWorker(TASK_QUEUE_SUBSCRIPTION).registerWorkflowImplementationTypes(
            SubscriptionWorkflowImpl::class.java,
        )
        // Start listening to the Task Queue.
        factory.start()
        return "STARTED"
    }
}