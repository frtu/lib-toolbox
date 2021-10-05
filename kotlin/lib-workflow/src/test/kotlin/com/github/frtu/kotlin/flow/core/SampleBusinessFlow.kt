package com.github.frtu.kotlin.flow.core

import java.lang.IllegalStateException

class SampleBusinessFlow(
    name: String,
    private val shouldSucceed: Boolean = true
) : AbstractFlow<Event, String>(name) {
    override fun extractId(event: Event): String = event.id.toString()

    override fun doValidation(event: Event) {
        checkNotNull(event.description, "description")
    }

    override fun doExecute(event: Event): String =
        if (shouldSucceed)
            "SUCCESS"
        else throw IllegalStateException("Error in BusinessFlow[$name]")
}