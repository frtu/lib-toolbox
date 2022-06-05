package com.github.frtu.workflow.temporal.common

import io.temporal.client.WorkflowClientOptions
import io.temporal.kotlin.TemporalDsl

/**
 * @see WorkflowClientOptions
 */
inline fun WorkflowClientOptionsExt(
    options: @TemporalDsl WorkflowClientOptions.Builder.() -> Unit
): WorkflowClientOptions {
    return WorkflowClientOptions.newBuilder().apply(options).build()
}

/**
 * Create a new instance of [WorkflowClientOptions], optionally overriding some of its properties.
 */
inline fun WorkflowClientOptions.copy(
    overrides: @TemporalDsl WorkflowClientOptions.Builder.() -> Unit
): WorkflowClientOptions {
    return toBuilder().apply(overrides).build()
}