package com.github.frtu.sample.execution

/**
 * A full workflow context capturing all event output states
 *
 * @author frtu
 */
class WorkflowContext(private val context: MutableList<WorkflowData> = mutableListOf()) {
    fun append(workflowData: WorkflowData) {
        context.add(workflowData)
    }

    fun get() = context.last()
}