package com.github.frtu.kotlin.flow.model

import com.github.frtu.kotlin.patterns.AbstractRegistry
import com.github.frtu.kotlin.patterns.UnrecognizedElementException

/**
 * A sample business flow defining : the Event, what is the ID and validation rules, how to execute request.
 * @author Frédéric TU
 * @since 1.1.4
 */
class SampleFlowRegistry(defaultElement: SampleFlow) :
    AbstractRegistry<SampleFlow>(
        "flow", mutableMapOf(defaultElement.name to defaultElement)
    ) {
    private val defaultKey = defaultElement.name

    override fun getElement(name: String): SampleFlow = try {
        super.getElement(name)
    } catch (e: UnrecognizedElementException) {
        super.getElement(defaultKey)
    }
}