package com.github.frtu.kotlin.flow.model

import com.github.frtu.kotlin.flow.core.AbstractFlow

/**
 * A sample business flow defining : the Event, what is the ID and validation rules, how to execute request.
 * @author Frédéric TU
 * @since 1.1.4
 */
open class SampleFlow(
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