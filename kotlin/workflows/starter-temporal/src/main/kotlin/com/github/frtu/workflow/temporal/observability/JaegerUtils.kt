package com.github.frtu.workflow.temporal.observability

import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.context.propagation.TextMapPropagator
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter
import io.opentelemetry.extension.trace.propagation.JaegerPropagator
import io.opentelemetry.opentracingshim.OpenTracingShim
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes
import io.opentracing.Tracer
import io.temporal.opentracing.OpenTracingOptions
import io.temporal.opentracing.OpenTracingSpanContextCodec
import java.util.concurrent.TimeUnit

object JaegerUtils {
    fun getJaegerOptions(serviceName: String, endpoint: String): OpenTracingOptions {
        val serviceNameResource: Resource = Resource.create(
            Attributes.of(ResourceAttributes.SERVICE_NAME, serviceName)
        )
        val jaegerExporter: JaegerGrpcSpanExporter = JaegerGrpcSpanExporter.builder()
            .setEndpoint(endpoint)
            .setTimeout(1, TimeUnit.SECONDS)
            .build()

        val tracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(jaegerExporter))
            .setResource(Resource.getDefault().merge(serviceNameResource))
            .build()
        val openTelemetry: OpenTelemetrySdk = OpenTelemetrySdk.builder()
            .setPropagators(
                ContextPropagators.create(
                    TextMapPropagator.composite(
                        W3CTraceContextPropagator.getInstance(), JaegerPropagator.getInstance()
                    )
                )
            )
            .setTracerProvider(tracerProvider)
            .build()

        // create OpenTracing shim and return OpenTracing Tracer from it
        return getOpenTracingOptionsForTracer(OpenTracingShim.createTracerShim(openTelemetry))
    }

    private fun getOpenTracingOptionsForTracer(tracer: Tracer): OpenTracingOptions {
        return OpenTracingOptions.newBuilder()
            .setSpanContextCodec(OpenTracingSpanContextCodec.TEXT_MAP_CODEC)
            .setTracer(tracer)
            .build()
    }
}