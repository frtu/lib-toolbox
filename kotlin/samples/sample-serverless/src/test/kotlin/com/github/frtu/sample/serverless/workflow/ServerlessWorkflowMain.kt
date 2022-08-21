package com.github.frtu.sample.serverless.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.utils.io.ResourceHelper
import io.serverlessworkflow.api.actions.Action
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.EventState
import io.serverlessworkflow.api.states.OperationState
import io.serverlessworkflow.api.states.SleepState
import io.serverlessworkflow.api.states.SwitchState
import io.serverlessworkflow.api.transitions.Transition
import io.serverlessworkflow.diagram.WorkflowDiagramImpl
import java.time.Duration
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

private val mapper: ObjectMapper = jacksonObjectMapper()

fun main() {
//    val definition = ResourceHelper().readFromFile("workflow/event/event-driven.sw.json")
    val definition = ResourceHelper().readFromFile("workflow/switch/greeting.sw.json")

    val serverlessWorkflow = ServerlessWorkflow.fromSource(definition)
    serverlessWorkflow.assertValidity()
    printJson(serverlessWorkflow)

//    val file = File("${serverlessWorkflow.name}.svg")
//    file.writeText(generateSvg(serverlessWorkflow))

    traverseState(serverlessWorkflow.getState(), serverlessWorkflow)
}

fun traverseState(workflowState: State?, serverlessWorkflow: ServerlessWorkflow) {
    workflowState?.let {
        println("-> Running state: [${workflowState.name}]")
        val nextState = executeStateAndReturnNext(workflowState, serverlessWorkflow)
        traverseState(nextState, serverlessWorkflow)
    }
}

fun executeStateAndReturnNext(workflowState: State, serverlessWorkflow: ServerlessWorkflow): State? =
    when (workflowState) {
        is EventState -> {
            val eventState = workflowState
            if (eventState.onEvents != null && eventState.onEvents.size > 0) {
                // TODO support more than 1 event (AND or OR) depending on eventState.isExclusive
                val firstEvent = eventState.onEvents[0]

                run(firstEvent.actions)
            }
            eventState.transition
        }
        is OperationState -> {
            val operationState = workflowState
            run(operationState.actions)
            operationState.transition
        }
        is SwitchState -> {
            val switchState = workflowState

            val transition: Transition = switchState.dataConditions.firstOrNull {
                println("filter(${it.condition})")
                true
            }?.let {
                it.transition
            } ?: switchState.defaultCondition.transition

            transition
        }
        is SleepState -> {
            val sleepState = workflowState
            sleepState.duration?.let { sleep(it) }
            sleepState.transition
        }
        else -> {
            println("Invalid or unsupported in demo dsl workflow state: $workflowState")
            null
        }
    }?.nextState
        ?.let { serverlessWorkflow.getStateWithName(it) }
        ?: null


fun run(actions: List<Action>) {
    if (!actions.isNullOrEmpty()) {
        for (action in actions) {
            run(action)
        }
    }
}

fun run(action: Action) {
    if (action.sleep != null && action.sleep.before != null) {
        sleep(action.sleep.before)
    }
    println("${action.functionRef.refName}(${action.functionRef.arguments})")
    if (action.sleep != null && action.sleep.after != null) {
        sleep(action.sleep.after)
    }
}

fun sleep(text: CharSequence) {
    sleep(Duration.parse(text))
}

fun sleep(duration: Duration) {
    Thread.sleep(duration.toMillis())
}

private fun printJson(bean: Any) {
    println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean))
}

private fun generateSvg(serverlessWorkflow: ServerlessWorkflow): String {
    val workflowDiagramImpl = WorkflowDiagramImpl()
    workflowDiagramImpl.setWorkflow(serverlessWorkflow)
    return workflowDiagramImpl.svgDiagram
}
