package com.github.frtu.sample.temporal.dynamicwkf

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.sample.temporal.activitydsl.model.ActResult
import com.github.frtu.sample.temporal.activitydsl.model.WorkflowContext
import com.github.frtu.sample.temporal.dynamicwkf.activity.ServerlessWorkflowActivity
import com.github.frtu.sample.temporal.dynamicwkf.activity.TASK_QUEUE_REGISTRY
import com.github.frtu.sample.temporal.dynamicwkf.serverless.*
import com.github.frtu.sample.temporal.dynamicwkf.utils.JQFilter
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import com.github.frtu.workflow.temporal.annotation.WorkflowImplementation
import io.serverlessworkflow.api.branches.Branch
import io.serverlessworkflow.api.events.OnEvents
import io.serverlessworkflow.api.functions.FunctionDefinition
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.*
import io.serverlessworkflow.api.transitions.Transition
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.common.converter.EncodedValues
import io.temporal.workflow.*
import io.temporal.workflow.Workflow.*
import java.time.Duration
import java.util.*
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

const val TASK_QUEUE_DSL = "TASK_QUEUE_DSL"

@WorkflowImplementation(taskQueue = TASK_QUEUE_DSL)
class DynamicDslWorkflow : DynamicWorkflow {
    lateinit var dslWorkflow: ServerlessWorkflow
    lateinit var activities: ActivityStub
    private var queryFunctions: List<FunctionDefinition>? = null

    private var workflowContext: WorkflowContext = WorkflowContext()

    override fun execute(args: EncodedValues): JsonNode? {
        // Get first input and convert to SW Workflow object
        val dslWorkflowId = args.get(0, String::class.java)
        val dslWorkflowVersion = args.get(1, String::class.java)

        // Using a global shared workflow object here is only allowed because its
        // assumed that at this point it is immutable and the same across all workflow worker restarts
        // FIXME : allow to deserialize ServerlessWorkflow
        dslWorkflow = ServerlessWorkflowRegistry.get(dslWorkflowId, dslWorkflowVersion)

        // Get second input which is set to workflowData
        val objectNode = args.get(2, JsonNode::class.java) as ObjectNode?
        objectNode?.let {
            workflowContext.value = objectNode
        }

        // Register dynamic signal handler
        // For demo signals input sets the workflowData
        // Improvement can be to add to it instead
        registerListener(
            DynamicSignalHandler { signalName: String, encodedArgs: EncodedValues ->
                if (workflowContext == null) {
                    workflowContext = WorkflowContext()
                }
                workflowContext.value = encodedArgs.get(0, JsonNode::class.java) as ObjectNode
            })

        // Get all expression type functions to be used for queries
        queryFunctions = dslWorkflow.getFunctionDefinitionsWithType(FunctionDefinition.Type.EXPRESSION)

        // Register dynamic query handler
        // we use expression type functions in workflow def as query definitions
        registerListener(
            label@ DynamicQueryHandler { queryType: String, encodedArgs: EncodedValues ->
                if (queryFunctions == null || dslWorkflow.getFunctionDefinitionWithName(queryType) == null) {
                    logger.warn("Unable to find expression function with name: $queryType")
                    val queryInput = encodedArgs.get(0, String::class.java)
                    if (queryInput.isNullOrEmpty()) {
                        // no input just return workflow data
                        return@DynamicQueryHandler workflowContext.value
                    } else {
                        return@DynamicQueryHandler JQFilter.instance?.evaluateExpression(
                            queryInput,
                            workflowContext.value
                        )
                    }
                }
                JQFilter.instance!!.evaluateExpression(
                    dslWorkflow.getFunctionDefinitionWithName(queryType)!!.operation,
                    workflowContext.value
                )
            })

        // Get the activity options that are set from properties in dsl
        val activityOptions: ActivityOptions = dslWorkflow.toActivityOptions(
            arrayOf(IllegalArgumentException::class.qualifiedName!!)
        )
        // Create a dynamic activities stub to be used for all actions in dsl
        activities = newUntypedActivityStub(activityOptions)

        // Start going through the dsl workflow states and execute depending on their instructions
        executeDslWorkflowFrom(dslWorkflow.getState())

        // Return the final workflow data as result
        return workflowContext.value
    }

    /** Executes workflow according to the dsl control flow logic  */
    private fun executeDslWorkflowFrom(dslWorkflowState: State?) {
        // This demo supports 3 states: Event State, Operation State and Switch state (data-based
        // switch)
        dslWorkflowState?.let {
            // execute the state and return the next workflow state depending on control flow logic in dsl
            // if next state is null it means that we need to stop execution
            val stateAndReturnNext = executeStateAndReturnNext(dslWorkflowState)
            executeDslWorkflowFrom(stateAndReturnNext)
        }
    }

    /**
     * Executes the control flow logic for a dsl workflow state. Demo supports EventState,
     * OperationState, and SwitchState currently. More can be added.
     */
    private fun executeStateAndReturnNext(dslWorkflowState: State): State? = when (dslWorkflowState) {
        is EventState -> {
            val eventState = dslWorkflowState as EventState
            // currently this demo supports only the first onEvents
            if (eventState.onEvents != null && eventState.onEvents.size > 0) {
                val firstEvent = eventState.onEvents[0]

                val eventStateActions = firstEvent.actions

                when (firstEvent.actionMode) {
                    // PARALLEL execution
                    OnEvents.ActionMode.PARALLEL -> {
                        val eventPromises = eventStateActions.map {
                            activities.executeAsync(
                                it.functionRef.refName,
                                ActResult::class.java,
                                workflowContext.customer
                            )
                        }.toList()

                        // Invoke all activities in parallel. Wait for all to complete
                        Promise.allOf(eventPromises).get()
                        for (promise in eventPromises) {
                            workflowContext.addResults(promise.get())
                        }
                    }
                    // SEQUENTIAL execution
                    else -> {
                        for (action in eventStateActions) {
                            if (action.sleep != null && action.sleep.before != null) {
                                sleep(Duration.parse(action.sleep.before))
                            }
                            // execute the action as an activity and assign its results to workflowData
                            workflowContext.addResults(
                                activities.execute(
                                    action.functionRef.refName,
                                    ActResult::class.java,
                                    workflowContext.customer
                                )
                            )
                            if (action.sleep != null && action.sleep.after != null) {
                                sleep(Duration.parse(action.sleep.after))
                            }
                        }
                    }
                }
            }
            eventState.transition?.nextState
                ?.let { dslWorkflow.getStateWithName(it) }
                ?: null
        }
        is OperationState -> {
            val operationState = dslWorkflowState as OperationState
            if (!operationState.actions.isNullOrEmpty()) {
                when (operationState.actionMode) {
                    // PARALLEL execution
                    OperationState.ActionMode.PARALLEL -> {
                        val actionsPromises = operationState.actions.map {
                            activities.executeAsync(
                                it.functionRef.refName,
                                ActResult::class.java,
                                workflowContext.customer
                            )
                        }.toList()
                        // Invoke all activities in parallel. Wait for all to complete
                        Promise.allOf(actionsPromises).get()
                        for (promise in actionsPromises) {
                            workflowContext.addResults(promise.get())
                        }
                    }
                    // SEQUENTIAL execution
                    else -> {
                        for (action in operationState.actions) {
                            if (action.sleep != null && action.sleep.before != null) {
                                sleep(Duration.parse(action.sleep.before))
                            }
                            // execute the action as an activity and assign its results to workflowData
                            workflowContext.addResults(
                                activities.execute(
                                    action.functionRef.refName,
                                    ActResult::class.java,
                                    workflowContext.customer
                                )
                            )
                            if (action.sleep != null && action.sleep.after != null) {
                                sleep(Duration.parse(action.sleep.after))
                            }
                        }
                    }
                }
            }
            operationState.transition?.nextState
                ?.let { dslWorkflow.getStateWithName(it) }
                ?: null
        }
        is SwitchState -> {
            // Demo supports only data based switch
            val switchState = dslWorkflowState as SwitchState

            val transition: Transition = switchState.dataConditions.firstOrNull {
                JQFilter.instance!!.evaluateBooleanExpression(it.condition, workflowContext.value)
            }?.let {
                it.transition
            } ?: switchState.defaultCondition.transition

            transition.nextState
                ?.let { dslWorkflow.getStateWithName(it) }
                ?: null
        }
        is SleepState -> {
            val sleepState = dslWorkflowState as SleepState
            sleepState.duration?.let {
                sleep(Duration.parse(it))
            }
            sleepState.transition?.nextState
                ?.let { dslWorkflow.getStateWithName(it) }
                ?: null
        }
        is ForEachState -> {
            val state = dslWorkflowState as ForEachState
            // List<Promise<JsonNode>> actionsPromises = new ArrayList<>();
            val inputs: List<JsonNode> = JQFilter.instance!!
                .evaluateArrayExpression(state.inputCollection, workflowContext.value)

            // TODO: update to exec all in parallel!
            for (ignored in inputs) {
                for (action in state.actions) {
                    if (action.sleep != null && action.sleep.before != null) {
                        sleep(Duration.parse(action.sleep.before))
                    }

                    // execute the action as an activity and assign its results to workflowData
                    workflowContext.addResults(
                        activities.execute(
                            action.functionRef.refName,
                            ActResult::class.java,
                            workflowContext.customer
                        )
                    )
                    if (action.sleep != null && action.sleep.after != null) {
                        sleep(Duration.parse(action.sleep.after))
                    }
                }
            }
            state.transition?.nextState
                ?.let { dslWorkflow.getStateWithName(it) }
                ?: null
        }
        is ParallelState -> {
            val parallelState = dslWorkflowState as ParallelState

            // this is just initial impl, still need to add things like timeouts etc
            // also this currently assumes the "allof" completion type (default)
            if (!parallelState.branches.isNullOrEmpty()) {
                val branchAllOfPromises: List<Promise<Void>> = parallelState.branches.map {
                    Async.procedure(
                        { branch: Branch -> processBranchActions(branch) }
                        , it
                    )
                }.toList()
                // execute all branch actions in parallel..wait for all to complete
                Promise.allOf(branchAllOfPromises).get()
            }
            parallelState.transition?.nextState
                ?.let { dslWorkflow.getStateWithName(it) }
                ?: null
        }
        else -> {
            logger.error("Invalid or unsupported in demo dsl workflow state: $dslWorkflowState")
            null
        }
    }

    private fun processBranchActions(branch: Branch) {
        // here we assume for now that all actions themselves inside
        // branch are also executed in parallel, just for sample sake
        // we should check the action mode to see if its sequential or parallel
        // will add...
        val branchActionPromises: MutableList<Promise<ActResult>> = ArrayList<Promise<ActResult>>()
        val branchActions = branch.actions
        for (action in branchActions) {
            branchActionPromises.add(
                activities.executeAsync(
                    action.functionRef.refName, ActResult::class.java, workflowContext.customer
                )
            )
        }
        Promise.allOf(branchActionPromises).get()
        for (promise in branchActionPromises) {
            workflowContext.addResults(promise.get())
        }
    }

    private val serverlessWorkflowRegistry = newActivityStub(
        ServerlessWorkflowActivity::class.java,
        ActivityOptions {
            // ActivityOptions DSL
            setTaskQueue(TASK_QUEUE_REGISTRY)
            setStartToCloseTimeout(Duration.ofSeconds(5)) // Timeout options specify when to automatically timeout Activities if the process is taking too long.
            // Temporal retries failures by default, this is simply an example.
            setRetryOptions(// RetryOptions specify how to automatically handle retries when Activities fail.
                RetryOptions {
                    setInitialInterval(Duration.ofMillis(100))
                    setMaximumInterval(Duration.ofSeconds(10))
                    setBackoffCoefficient(2.0)
                    setMaximumAttempts(10)
                })
        },
        // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
        mapOf(
            WORKFLOW_REGISTRY to ActivityOptions { setHeartbeatTimeout(Duration.ofSeconds(5)) }
        )
    )

    private fun fetchUserIds(subscriptionEvent: SubscriptionEvent) = (1..2)
        .map { UUID.randomUUID() }
        .toCollection(mutableListOf())

    private val logger = Workflow.getLogger(DynamicDslWorkflow::class.java)
    private val structuredLogger = StructuredLogger.create(this::class.java)

    companion object {
        private const val WORKFLOW_REGISTRY = "WorkflowRegistry"
    }
}
