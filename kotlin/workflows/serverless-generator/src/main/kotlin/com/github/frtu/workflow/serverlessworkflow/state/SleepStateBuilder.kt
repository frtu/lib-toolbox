package com.github.frtu.workflow.serverlessworkflow.state

import io.serverlessworkflow.api.states.DefaultState.Type.SLEEP
import io.serverlessworkflow.api.states.SleepState

/**
 * Builder for sleep state
 *
 * @author frtu
 * @since 1.2.5
 */
class SleepStateBuilder(
    name: String? = null,
) : AbstractStateBuilder<SleepState>(SleepState(), SLEEP, name) {

    var duration: String?
        get() = model.duration
        set(value) {
            logger.trace("Sleep: duration={}", value)
            model.withDuration(value)
        }
}

fun sleep(name: String? = null, duration: String? = null, options: SleepStateBuilder.() -> Unit = {}): SleepState =
    SleepStateBuilder(name).apply {
        duration?.let { this.duration = duration }
    }.apply(options).build()