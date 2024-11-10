package com.github.frtu.kotlin.tool.execution.durable.temporal.stub

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.tool.StructuredToolExecuter
import com.github.frtu.kotlin.tool.ToolExecuter
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
    workflowId: String,
    /** Description that can be used by agent to decide which tool to use */
    description: String,
    /** Input parameter schema */
    private val parameterClass: Class<INPUT>,
    /** Return schema. `null` schema when returning `void` */
    private val returnClass: Class<OUTPUT>?,
    /** Stub */
    private val workflowClient: WorkflowClient,
    private val defaultWorkflowOptions: WorkflowOptions,
    private val timeoutInSec: Long = 10L,
) : ToolExecuter(
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
        workflowId = workflowId,
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
        logger.info("Started workflow execution call id:[${workflowExecution.workflowId}] run id:[${workflowExecution.runId}]")

        return if (returnClass != null && returnClass != Void::javaClass) {
            logger.debug("Preparing to receive call from type:$returnClass")
            try {
                workflowStub.getResult(timeoutInSec, TimeUnit.SECONDS, JsonNode::class.java).also {
                    logger.info("Workflow execution call returned:$it")
                }
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