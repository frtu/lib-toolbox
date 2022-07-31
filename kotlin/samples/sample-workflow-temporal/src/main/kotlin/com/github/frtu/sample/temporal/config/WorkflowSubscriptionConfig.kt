package com.github.frtu.sample.temporal.config

import com.github.frtu.sample.domain.EmailCrudHandler
import com.github.frtu.sample.sink.database.EmailDatabaseSink
import com.github.frtu.sample.temporal.activity.EmailSinkActivity
import com.github.frtu.sample.temporal.activity.TASK_QUEUE_EMAIL
import com.github.frtu.sample.temporal.staticwkf.workflow.SubscriptionWorkflowImpl
import com.github.frtu.sample.temporal.staticwkf.workflow.TASK_QUEUE_SUBSCRIPTION
import io.temporal.worker.WorkerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkflowSubscriptionConfig {
    @Bean
    fun emailSink(emailCrudHandler: EmailCrudHandler) = EmailDatabaseSink(emailCrudHandler)

    /**
     * There should ONLY have one worker per application
     */
    @Bean
    fun worker(factory: WorkerFactory, emailSinkActivity: EmailSinkActivity): String {
        factory.newWorker(TASK_QUEUE_SUBSCRIPTION).apply {
            this.registerWorkflowImplementationTypes(SubscriptionWorkflowImpl::class.java)
        }
        factory.newWorker(TASK_QUEUE_EMAIL).apply {
            this.registerActivitiesImplementations(
                emailSinkActivity
            )
        }
        // Start listening to the Task Queue.
        factory.start()
        return "STARTED"
    }
}