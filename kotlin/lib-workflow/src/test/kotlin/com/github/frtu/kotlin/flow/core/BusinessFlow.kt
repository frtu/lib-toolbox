package com.github.frtu.kotlin.flow.core

class BusinessFlow(
    flowName: String,
) : BaseFlow<Event>(flowName) {
    override fun extractId(event: Event): String = event.id.toString()

    override fun doValidation(event: Event) {
        checkNotNull(event.description, "description")
    }

    override fun doExecute(event: Event) {
        TODO("Not yet implemented")
    }
}