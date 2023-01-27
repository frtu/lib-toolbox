package com.github.frtu.workflow.serverlessworkflow.state

import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SleepState

private fun <STATE : DefaultState, BUILDER : AbstractStateBuilder<STATE>> build(
    stateBuilder: BUILDER, options: BUILDER.() -> Unit
): STATE {
    stateBuilder.apply(options)
    return stateBuilder.build()
}

fun sleep(stateName: String? = null, duration: String? = null, options: SleepStateBuilder.() -> Unit = {}): SleepState =
    SleepStateBuilder(stateName).apply {
        duration?.let { this.duration = duration }
    }.apply(options).build()