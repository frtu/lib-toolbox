package com.github.frtu.sample.temporal.config

import com.github.frtu.sample.domain.EmailCrudHandler
import com.github.frtu.sample.sink.database.EmailDatabaseSink
import com.github.frtu.sample.temporal.dynamicwkf.serverless.ServerlessWorkflowRegistry
import com.github.frtu.sample.temporal.dynamicwkf.utils.IoHelper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkflowSpecificConfig {
    @Bean
    fun emailSink(emailCrudHandler: EmailCrudHandler) = EmailDatabaseSink(emailCrudHandler)

    @Bean
    fun serverlessWorkflow() =
        ServerlessWorkflowRegistry.register(IoHelper.getFileAsString("dsl/customerapplication/workflow.json"))

//    --- PayloadConverter already exists in DefaultDataConverter#DEFAULT_PAYLOAD_CONVERTERS ---
//    => Uncomment to specialize it
//
//    @Bean
//    fun nullPayloadConverter() = NullPayloadConverter()
//
//    @Bean
//    fun byteArrayPayloadConverter() = ByteArrayPayloadConverter()
//
//    @Bean
//    fun jacksonJsonPayloadConverter() = JacksonJsonPayloadConverter()
//
//    @Bean
//    fun protobufJsonPayloadConverter() = ProtobufJsonPayloadConverter()
//
//    @Bean
//    fun protobufPayloadConverter() = ProtobufPayloadConverter()
}