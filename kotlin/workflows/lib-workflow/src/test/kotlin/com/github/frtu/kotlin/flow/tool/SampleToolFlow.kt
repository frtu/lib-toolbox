package com.github.frtu.kotlin.flow.tool

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.frtu.kotlin.flow.model.Event
import com.github.frtu.kotlin.utils.io.toObject

class SampleToolFlow(
    private val shouldSucceed: Boolean = true
) : ToolFlow<Event, String>(
    name = "Flow name",
    description = "Flow to be executed as Tool",
    parameterClass = Event::class.java,
    returnClass = String::class.java,
) {
    override fun extractId(event: Event): String = event.id.toString()

    override fun doValidation(event: Event) {
        checkNotNull(event.description, "description")
    }

    override fun doExecute(event: Event): String =
        if (shouldSucceed)
            "SUCCESS"
        else throw IllegalStateException("Error in BusinessFlow[$flowName]")
}