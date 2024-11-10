package com.github.frtu.kotlin.tool.execution.durable.temporal.stub

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.tool.StructuredToolExecuter
import com.github.frtu.kotlin.tool.ToolExecuter
import io.temporal.activity.ActivityOptions
import io.temporal.failure.ActivityFailure
import io.temporal.workflow.Workflow

/**
 * Activity call, eventually can be used as handoff
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
open class ActivityCallAsTool<INPUT, OUTPUT>(
    /** Id of the activity */
    activityId: String,
    /** Description that can be used by agent to decide which tool to use */
    description: String,
    /** Input parameter schema */
    parameterClass: Class<INPUT>,
    /** Return schema. `null` schema when returning `void` */
    returnClass: Class<OUTPUT>?,
    /** Stub */
    private val defaultActivityOptions: ActivityOptions,
) : ToolExecuter(
    id = activityId,
    description = description,
    parameterClass = parameterClass,
    returnClass = returnClass,
), GenericAction {
    constructor(
        activityId: String,
        description: String,
        parameterClass: Class<INPUT>,
        returnClass: Class<OUTPUT>?,
        taskQueue: String = activityId,
    ) : this(
        activityId = activityId,
        description = description,
        parameterClass = parameterClass,
        returnClass = returnClass,
        defaultActivityOptions = ActivityOptions {
            setTaskQueue(taskQueue)
        },
    )

    override suspend fun execute(parameter: JsonNode): JsonNode {
        val activityStub = Workflow.newUntypedActivityStub(defaultActivityOptions)
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