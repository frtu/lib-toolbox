package com.github.frtu.sample.temporal.config

import com.github.frtu.sample.domain.EmailCrudHandler
import com.github.frtu.sample.sink.database.EmailDatabaseSink
import com.github.frtu.sample.temporal.dynamicwkf.serverless.ServerlessWorkflowRegistry
import com.github.frtu.sample.temporal.dynamicwkf.utils.IoHelper
import com.github.frtu.workflow.temporal.config.WorkerRegistrationConfig
import io.temporal.common.converter.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
//@Import(WorkerRegistrationConfig::class)
class WorkerConfig {
    @Bean
    fun emailSink(emailCrudHandler: EmailCrudHandler) = EmailDatabaseSink(emailCrudHandler)

    @Bean
    fun serverlessWorkflow() =
        ServerlessWorkflowRegistry.register(IoHelper.getFileAsString("dsl/customerapplication/workflow.json"))

    @Bean
    fun nullPayloadConverter() = NullPayloadConverter()

    @Bean
    fun byteArrayPayloadConverter() = ByteArrayPayloadConverter()

    @Bean
    fun jacksonJsonPayloadConverter() = JacksonJsonPayloadConverter()

    @Bean
    fun protobufJsonPayloadConverter() = ProtobufJsonPayloadConverter()

    @Bean
    fun protobufPayloadConverter() = ProtobufPayloadConverter()

    /**
     * There should ONLY have one worker per application
     */
//    @Bean
//    fun worker(
//        factory: WorkerFactory,
//        emailSinkActivity: EmailSinkActivity,
//        serverlessWorkflowActivity: ServerlessWorkflowActivity,
//    ): String {
//        factory.newWorker(TASK_QUEUE_DSL).apply {
//            this.registerWorkflowImplementationTypes(DynamicDslWorkflow::class.java)
//            this.registerActivitiesImplementations(DslActivitiesImpl())
//        }
//        factory.newWorker(TASK_QUEUE_REGISTRY).apply {
//            this.registerActivitiesImplementations(serverlessWorkflowActivity)
//        }
//        factory.newWorker(TASK_QUEUE_SUBSCRIPTION).apply {
//            this.registerWorkflowImplementationTypes(SubscriptionWorkflowImpl::class.java)
//        }
//        factory.newWorker(TASK_QUEUE_EMAIL).apply {
//            this.registerActivitiesImplementations(
//                emailSinkActivity
//            )
//        }
//        // Start listening to the Task Queue.
//        factory.start()
//        return "STARTED"
//    }
}