package com.github.frtu.sample.temporal.config

import com.github.frtu.sample.domain.EmailCrudHandler
import com.github.frtu.sample.sink.database.EmailDatabaseSink
import com.github.frtu.sample.temporal.activity.EmailSinkActivity
import com.github.frtu.sample.temporal.activity.TASK_QUEUE_EMAIL
import com.github.frtu.sample.temporal.activitydsl.DslActivitiesImpl
import com.github.frtu.sample.temporal.dynamicwkf.DynamicDslWorkflow
import com.github.frtu.sample.temporal.dynamicwkf.TASK_QUEUE_DSL
import com.github.frtu.sample.temporal.dynamicwkf.activity.ServerlessWorkflowActivity
import com.github.frtu.sample.temporal.dynamicwkf.activity.TASK_QUEUE_REGISTRY
import com.github.frtu.sample.temporal.dynamicwkf.serverless.ServerlessWorkflowRegistry
import com.github.frtu.sample.temporal.dynamicwkf.utils.IoHelper
import com.github.frtu.sample.temporal.staticwkf.workflow.SubscriptionWorkflowImpl
import com.github.frtu.sample.temporal.staticwkf.workflow.TASK_QUEUE_SUBSCRIPTION
import io.temporal.worker.WorkerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkerConfig {
    @Bean
    fun emailSink(emailCrudHandler: EmailCrudHandler) = EmailDatabaseSink(emailCrudHandler)

    @Bean
    fun serverlessWorkflow() =
        ServerlessWorkflowRegistry.register(IoHelper.getFileAsString("dsl/customerapplication/workflow.json"))

    /**
     * There should ONLY have one worker per application
     */
    @Bean
    fun worker(
        factory: WorkerFactory,
        emailSinkActivity: EmailSinkActivity,
        serverlessWorkflowActivity: ServerlessWorkflowActivity,
    ): String {
        factory.newWorker(TASK_QUEUE_DSL).apply {
            this.registerWorkflowImplementationTypes(DynamicDslWorkflow::class.java)
            this.registerActivitiesImplementations(DslActivitiesImpl())
        }
        factory.newWorker(TASK_QUEUE_REGISTRY).apply {
            this.registerActivitiesImplementations(serverlessWorkflowActivity)
        }
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