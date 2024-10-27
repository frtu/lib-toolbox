package com.github.frtu.kotlin.llm.os.agent.durable.temporal.activity

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.llm.os.tool.ToolExecuter
import io.temporal.activity.ActivityOptions
import io.temporal.failure.ActivityFailure
import io.temporal.workflow.Workflow

/**
 * Activity call, eventually can be used as handoff
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
class ActivityCallAsTool<INPUT, OUTPUT>(
    /** Id of the activity */
    activityId: ActionId,
    /** Description that can be used by agent to decide which tool to use */
    description: String,
    /** Input parameter schema */
    parameterJsonSchema: String,
    /** Return schema. `null` schema when returning `void` */
    returnJsonSchema: String? = null,
    /** Stub */
    private val taskQueue: String,
) : ToolExecuter(
    id = activityId,
    description = description,
    parameterJsonSchema = parameterJsonSchema,
    returnJsonSchema = returnJsonSchema,
), GenericAction {
    override suspend fun execute(parameter: JsonNode): JsonNode {
        val activityStub = Workflow.newUntypedActivityStub(
            ActivityOptions {
                setTaskQueue(taskQueue)
            }
        )
        val result = try {
            activityStub.execute(id.value, JsonNode::class.java, parameter)
        } catch (e: ActivityFailure) {
            logger.error("Activity failure id:${e.activityId} type:${e.activityType} msg:${e.message}", e)
            throw e
        } catch (e: Exception) {
            logger.error(e.message, e)
            throw e
        }
        return result
    }
}