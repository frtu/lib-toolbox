package com.github.frtu.workflow.temporal.config

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.message
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext
import io.temporal.api.enums.v1.QueryRejectCondition
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.context.ContextPropagator
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.PayloadConverter
import io.temporal.common.interceptors.WorkerInterceptor
import io.temporal.common.interceptors.WorkflowClientInterceptor
import io.temporal.serviceclient.GrpcMetadataProvider
import io.temporal.serviceclient.RpcRetryOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.WorkerFactory
import io.temporal.worker.WorkerFactoryOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer
import com.uber.m3.tally.Scope as MetricScope

/**
 * Configure Temporal stub, factory, client
 *
 * @author Frédéric TU
 * @since 1.2.1
 */
@Configuration
class TemporalConfig {
    @Bean
    fun workflowServiceStubsOptionsBean(
        temporalStubProperties: TemporalStubProperties,
        rpcRetryOptions: RpcRetryOptions?,
        channelInitializer: Consumer<ManagedChannelBuilder<*>>?,
        grpcMetadataProviders: Collection<GrpcMetadataProvider>?,
        grpcClientInterceptors: Collection<ClientInterceptor>?,
        metricScope: MetricScope?,
        headers: Metadata?,
        sslContext: SslContext?,
    ): WorkflowServiceStubsOptions = WorkflowServiceStubsOptions {
        temporalStubProperties.target?.apply { setTarget(this) }
        temporalStubProperties.enableHttps?.apply { setEnableHttps(this) }
        sslContext?.apply { setSslContext(this) }

        channelInitializer?.apply { setChannelInitializer(this) }
        grpcMetadataProviders?.apply { setGrpcMetadataProviders(this) }
        grpcClientInterceptors?.apply { setGrpcClientInterceptors(this) }
        metricScope?.apply { setMetricsScope(this) }

        headers?.apply { setHeaders(this) }

        temporalStubProperties.enableKeepAlive?.apply { setEnableKeepAlive(this) }
        temporalStubProperties.keepAliveTime?.apply { setKeepAliveTime(this) }
        temporalStubProperties.keepAliveTimeout?.apply { setKeepAliveTimeout(this) }
        temporalStubProperties.keepAlivePermitWithoutStream?.apply { setKeepAlivePermitWithoutStream(this) }
        temporalStubProperties.connectionBackoffResetFrequency?.apply { setConnectionBackoffResetFrequency(this) }
        temporalStubProperties.grpcReconnectFrequency?.apply { setGrpcReconnectFrequency(this) }

        temporalStubProperties.rpcTimeout?.apply { setRpcTimeout(this) }
        temporalStubProperties.rpcQueryTimeout?.apply { setRpcQueryTimeout(this) }
        temporalStubProperties.rpcLongPollTimeout?.apply { setRpcLongPollTimeout(this) }

        rpcRetryOptions?.apply { setRpcRetryOptions(rpcRetryOptions) }
    }

    @Bean
    fun workflowServiceStubs(workflowServiceStubsOptions: WorkflowServiceStubsOptions): WorkflowServiceStubs =
        WorkflowServiceStubs.newServiceStubs(workflowServiceStubsOptions)

    @Bean
    fun workflowClient(
        service: WorkflowServiceStubs,
        temporalStubProperties: TemporalStubProperties,
        workflowClientInterceptors: List<WorkflowClientInterceptor>,
        payloadConverters: List<PayloadConverter>?,
        contextPropagators: List<ContextPropagator>?,
        queryRejectCondition: QueryRejectCondition?,
    ): WorkflowClient =
        WorkflowClient.newInstance(service, WorkflowClientOptions {
            val identity = temporalStubProperties.identity
            structuredLogger.info(message("Setting identity[$identity] namespace[${temporalStubProperties.namespace}] workflowClientInterceptors[${workflowClientInterceptors.size}] workflowClientInterceptors[${workflowClientInterceptors.size}]"))
            identity?.let { setIdentity(identity) }
            setNamespace(temporalStubProperties.namespace)
            setInterceptors(*workflowClientInterceptors.toTypedArray())
            temporalStubProperties.binaryChecksum?.let { setBinaryChecksum(temporalStubProperties.binaryChecksum) }
            if (!payloadConverters.isNullOrEmpty()) {
                setDataConverter(DefaultDataConverter(*payloadConverters.toTypedArray()))
            }
            if (!contextPropagators.isNullOrEmpty()) {
                setContextPropagators(contextPropagators)
            }
            queryRejectCondition?.let { setQueryRejectCondition(queryRejectCondition) }
        })

    @Bean
    fun workerFactory(client: WorkflowClient, workerInterceptors: List<WorkerInterceptor>): WorkerFactory =
        WorkerFactory.newInstance(client, WorkerFactoryOptions {
            structuredLogger.info(message("Setting workerInterceptors[${workerInterceptors.size}]"))
            setWorkerInterceptors(*workerInterceptors.toTypedArray())
        })

    private val structuredLogger = StructuredLogger.create(this::class.java)
}