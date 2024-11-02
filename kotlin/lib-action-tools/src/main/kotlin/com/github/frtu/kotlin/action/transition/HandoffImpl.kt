package com.github.frtu.kotlin.action.transition

import com.github.frtu.kotlin.action.execution.TypedAction
import com.github.frtu.kotlin.action.execution.transition.ActionTransitionImpl
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.action.management.ActionMetadata

/**
 * Handoff input is a class that return an `ActionMetadata`
 */
open class HandoffImpl<INPUT, ACTION : ActionMetadata>(
    parameterJsonSchema: String,
    nextAction: ACTION,
    id: ActionId = ActionId("handoff-to-${nextAction.id}"),
    description: String = "Transfer to '${nextAction.id}' if the user wants ${nextAction.description}",
) : TypedAction<INPUT, ACTION>, Handoff<INPUT, ACTION>, ActionTransitionImpl<INPUT, ACTION>(
    parameterJsonSchema = parameterJsonSchema,
    nextAction = nextAction,
    id = id,
    description = description,
) {
    override suspend fun execute(parameter: INPUT): ACTION = nextAction
}