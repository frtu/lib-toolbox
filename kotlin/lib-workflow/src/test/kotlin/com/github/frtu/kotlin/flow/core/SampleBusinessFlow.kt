package com.github.frtu.kotlin.flow.core

import java.lang.IllegalStateException

class SampleBusinessFlow(
    flowName: String,
    private val shouldSucceed: Boolean = true
) : BaseFlow<Event, String>(flowName) {
    override fun extractId(event: Event): String = event.id.toString()

    override fun doValidation(event: Event) {
        checkNotNull(event.description, "description")
    }

    override fun doExecute(event: Event): String =
        if (shouldSucceed)
            "SUCCESS"
        else throw IllegalStateException("Error in BusinessFlow[$flowName]")
}