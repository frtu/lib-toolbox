package com.github.frtu.kotlin.tool.execution.durable.temporal.workflow

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.tool.StructuredToolExecuter
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.client.copy
import java.nio.charset.Charset
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Encapsulate workflow call as a Tool
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
class WorkflowCallAsTool<INPUT, OUTPUT>(
    /** Id of the workflow */
    workflowId: ActionId,
    /** Description that can be used by agent to decide which tool to use */
    description: String,
    /** Input parameter schema */
    parameterClass: Class<INPUT>,
    /** Return schema. `null` schema when returning `void` */
    returnClass: Class<OUTPUT>?,
    /** Stub */
    private val workflowClient: WorkflowClient,
    private val defaultWorkflowOptions: WorkflowOptions,
    private val timeoutInSec: Long = 10L,
) : StructuredToolExecuter<INPUT, OUTPUT>(
    id = workflowId,
    description = description,
    parameterClass = parameterClass,
    returnClass = returnClass,
), GenericAction {
    constructor(
        workflowId: String,
        description: String,
        parameterClass: Class<INPUT>,
        returnClass: Class<OUTPUT>?,
        workflowClient: WorkflowClient,
        taskQueue: String,
    ) : this(
        workflowId = ActionId(workflowId),
        description = description,
        parameterClass = parameterClass,
        returnClass = returnClass,
        workflowClient = workflowClient,
        defaultWorkflowOptions = WorkflowOptions {
            setTaskQueue(taskQueue)
        },
    )

    override suspend fun execute(parameter: JsonNode): JsonNode {
        val workflowInstanceId =
            UUID.nameUUIDFromBytes(parameter.toString().toByteArray(Charset.defaultCharset())).toString()

        val workflowStub = workflowClient.newUntypedWorkflowStub(id.value, defaultWorkflowOptions.copy {
            setWorkflowId(workflowInstanceId)
        })
        val workflowExecution = workflowStub.start(parameter)
        logger.info("Started workflow execution id:[${workflowExecution.workflowId}] run id:[${workflowExecution.runId}]")

        return if (returnClass != null && returnClass != Void::javaClass) {
            logger.info("Preparing to receive call from type:$returnClass")
            try {
                workflowStub.getResult(timeoutInSec, TimeUnit.SECONDS, JsonNode::class.java)
            } catch (e: TimeoutException) {
                logger.error("Workflow encounter timeout of $timeoutInSec sec. Error message:${e.message}", e)
                NullNode.instance
            } catch (e: Exception) {
                logger.error(e.message, e)
                throw e
            }
        } else {
            NullNode.instance
        }
    }
}