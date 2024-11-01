package com.github.frtu.kotlin.llm.os.agent.durable.temporal.workflow

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.llm.os.tool.ToolExecuter
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
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
    parameterJsonSchema: String,
    /** Return schema. `null` schema when returning `void` */
    returnJsonSchema: String? = null,
    /** Stub */
    private val workflowClient: WorkflowClient,
    private val workflowOptions: WorkflowOptions,
) : ToolExecuter(
    id = workflowId,
    description = description,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
), GenericAction {
    override suspend fun execute(parameter: JsonNode): JsonNode {
        val workflowStub = workflowClient.newUntypedWorkflowStub(id.value, workflowOptions)
        val workflowExecution = workflowStub.start(parameter)
        logger.info("Started workflow execution id:[${workflowExecution.workflowId}] run id:[${workflowExecution.runId}]")

        return try {
            workflowStub.getResult(10, TimeUnit.SECONDS, JsonNode::class.java)
        } catch (e: TimeoutException) {
            NullNode.instance
        } catch (e: Exception) {
            logger.error(e.message, e)
            throw e
        }
    }
}