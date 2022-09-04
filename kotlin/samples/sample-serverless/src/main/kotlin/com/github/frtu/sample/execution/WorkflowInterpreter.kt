package com.github.frtu.sample.execution

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.sample.execution.expression.JsonNodeExpressionInterpreter
import com.github.frtu.sample.execution.expression.spel.SpelExpressionInterpreter
import com.github.frtu.sample.serverless.workflow.getStartingState
import com.github.frtu.sample.serverless.workflow.getStateWithName
import io.serverlessworkflow.api.actions.Action
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.EventState
import io.serverlessworkflow.api.states.OperationState
import io.serverlessworkflow.api.states.SleepState
import io.serverlessworkflow.api.states.SwitchState
import io.serverlessworkflow.api.transitions.Transition
import org.slf4j.LoggerFactory
import java.time.Duration
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

/**
 * Workflow Interpreter for Serverless Workflow
 *
 * @author frtu
 */
open class WorkflowInterpreter(
    private val serverlessWorkflow: ServerlessWorkflow,
    private val workflowContext: WorkflowContext = WorkflowContext(),
    private val expressionInterpreter: JsonNodeExpressionInterpreter = SpelExpressionInterpreter(),
) {
    /**
     * Trigger workflow start
     */
    fun start(data: JsonNode) {
        workflowContext.append(WorkflowData(data))
        traverseState(serverlessWorkflow.getStartingState(), serverlessWorkflow)
    }

    /**
     * Internal recursive state traversal
     */
    protected fun traverseState(workflowState: State?, serverlessWorkflow: ServerlessWorkflow) {
        workflowState?.let {
            logger.debug("-> Running state: [${workflowState.name}]")
            val nextState = executeStateAndReturnNext(workflowState, serverlessWorkflow)
            traverseState(nextState, serverlessWorkflow)
        }
    }

    /**
     * Internal recursive state traversal
     */
    protected fun executeStateAndReturnNext(
        workflowState: State,
        serverlessWorkflow: ServerlessWorkflow,
    ): State? =
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
                    val condition = it.condition
                    logger.debug("filter($condition)")
                    false
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


    protected fun run(actions: List<Action>) {
        if (!actions.isNullOrEmpty()) {
            for (action in actions) {
                run(action)
            }
        }
    }

    protected fun run(action: Action) {
        if (action.sleep != null && action.sleep.before != null) {
            sleep(action.sleep.before)
        }
        println("${action.functionRef.refName}(${action.functionRef.arguments})")
        if (action.sleep != null && action.sleep.after != null) {
            sleep(action.sleep.after)
        }
    }

    protected fun sleep(text: CharSequence) {
        sleep(Duration.parse(text))
    }

    protected fun sleep(duration: Duration) {
        Thread.sleep(duration.toMillis())
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}