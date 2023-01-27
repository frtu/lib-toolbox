package com.github.frtu.workflow.temporal.common

import io.temporal.kotlin.TemporalDsl
import io.temporal.worker.WorkerFactoryOptions

/**
 * @see WorkerFactoryOptions
 */
inline fun WorkerFactoryOptionsExt(
    options: @TemporalDsl WorkerFactoryOptions.Builder.() -> Unit
): WorkerFactoryOptions {
    return WorkerFactoryOptions.newBuilder().apply(options).build()
}

/**
 * Create a new instance of [WorkerFactoryOptions], optionally overriding some of its properties.
 */
inline fun WorkerFactoryOptions.copy(
    overrides: @TemporalDsl WorkerFactoryOptions.Builder.() -> Unit
): WorkerFactoryOptions {
    return toBuilder().apply(overrides).build()
}