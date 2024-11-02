package com.github.frtu.kotlin.action.tool.transition

import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.action.tool.Tool
import com.github.frtu.kotlin.action.transition.HandoffImpl

/**
 * Handoff input is a class that return an `ActionMetadata`
 */
class HandoffToTool(
    nextAction: Tool,
    id: ActionId = ActionId("handoff-to-${nextAction.id}"),
    description: String = "Transfer to '${nextAction.id}' if the user wants ${nextAction.description}",
) : HandoffImpl<Unit, Tool>(
    parameterJsonSchema = "SCHEMA FOR NOTHING NEEDED",
    nextAction = nextAction,
    id = id,
    description = description,
) {
    override suspend fun execute(parameter: Unit): Tool = nextAction
}