package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for Builder
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
abstract class AbstractBuilder<MODEL>(
    protected val model: MODEL,
    name: String?,
    transition: String? = null,
) {
    init {
        assignName(name)
        assignTransition(transition)
    }

    @DslBuilder
    open var name: String
        get() { TODO() }
        set(value) { assignName(value) }

    abstract fun assignName(value: String?): Unit

    @DslBuilder
    open var transition: String?
        get() { TODO() }
        set(value) { assignTransition(value) }

    abstract fun assignTransition(value: String?): Unit

    open fun build(): MODEL = model

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
}