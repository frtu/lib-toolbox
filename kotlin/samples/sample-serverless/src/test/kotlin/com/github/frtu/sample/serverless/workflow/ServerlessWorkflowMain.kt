package com.github.frtu.sample.serverless.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.utils.io.ResourceHelper
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.EventState
import io.serverlessworkflow.api.states.OperationState
import io.serverlessworkflow.api.states.SleepState
import io.serverlessworkflow.api.states.SwitchState
import io.serverlessworkflow.api.transitions.Transition
import io.serverlessworkflow.diagram.WorkflowDiagramImpl
import java.io.File
import java.time.Duration
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

private val mapper: ObjectMapper = jacksonObjectMapper()

fun main() {
//    val definition = ResourceHelper().readFromFile("workflow/event/event-driven.sw.json")
    val definition = ResourceHelper().readFromFile("workflow/switch/greeting.sw.json")

    val serverlessWorkflow = ServerlessWorkflow.fromSource(definition)
    serverlessWorkflow.assertValidity()
    printJson(serverlessWorkflow)

    val file = File("${serverlessWorkflow.name}.svg")
    file.writeText(generateSvg(serverlessWorkflow))

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

                val eventStateActions = firstEvent.actions

                for (action in eventStateActions) {
                    if (action.sleep != null && action.sleep.before != null) {
                        sleep(Duration.parse(action.sleep.before))
                    }
                    println("execute(${action.functionRef.refName})")
                    if (action.sleep != null && action.sleep.after != null) {
                        sleep(Duration.parse(action.sleep.after))
                    }
                }
            }
            eventState.transition?.nextState
                ?.let { serverlessWorkflow.getStateWithName(it) }
                ?: null
        }
        is OperationState -> {
            val operationState = workflowState
            if (!operationState.actions.isNullOrEmpty()) {
                for (action in operationState.actions) {
                    if (action.sleep != null && action.sleep.before != null) {
                        sleep(Duration.parse(action.sleep.before))
                    }
                    println("execute(${action.functionRef.refName})")
                    if (action.sleep != null && action.sleep.after != null) {
                        sleep(Duration.parse(action.sleep.after))
                    }
                }
            }
            operationState.transition?.nextState
                ?.let { serverlessWorkflow.getStateWithName(it) }
                ?: null
        }
        is SwitchState -> {
            val switchState = workflowState

            val transition: Transition = switchState.dataConditions.firstOrNull {
                println("execute(${it.condition})")
                true
            }?.let {
                it.transition
            } ?: switchState.defaultCondition.transition

            transition.nextState
                ?.let { serverlessWorkflow.getStateWithName(it) }
                ?: null
        }
        is SleepState -> {
            val sleepState = workflowState
            sleepState.duration?.let {
                sleep(Duration.parse(it))
            }
            sleepState.transition?.nextState
                ?.let { serverlessWorkflow.getStateWithName(it) }
                ?: null
        }
        else -> {
            println("Invalid or unsupported in demo dsl workflow state: $workflowState")
            null
        }
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
