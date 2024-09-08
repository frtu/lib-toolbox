package com.github.frtu.kotlin.flow.model

import com.github.frtu.kotlin.patterns.AbstractRegistry
import com.github.frtu.kotlin.patterns.UnrecognizedElementException

/**
 * A sample business flow defining : the Event, what is the ID and validation rules, how to execute request.
 * @author Frédéric TU
 * @since 1.1.4
 */
class SampleFlowRegistry(defaultElement: SampleFlow) :
    AbstractRegistry<String, SampleFlow>(
        "flow", mutableMapOf(defaultElement.name to defaultElement)
    ) {
    private val defaultKey = defaultElement.name

    public override fun register(name: String, element: SampleFlow) =
        super.register(name, element) as SampleFlowRegistry

    override operator fun get(name: String): SampleFlow = try {
        super.get(name)
    } catch (e: UnrecognizedElementException) {
        super.get(defaultKey)
    }
}