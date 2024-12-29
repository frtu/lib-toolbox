package com.github.frtu.kotlin.action.execution.transition

import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.action.management.ActionMetadata

/**
 * ActionTransitionImpl input is a class that return an `ActionMetadata`
 */
open class ActionTransitionImpl<INPUT, ACTION : ActionMetadata>(
    override val parameterJsonSchema: String,
    protected val nextAction: ACTION,
    override val id: ActionId = ActionId("transition-to-${nextAction.id}"),
    override val description: String = "Allow to transition from the current action to ${nextAction.id}",
    override val category: String? = null,
    override val subCategory: String? = null,
) : ActionTransition<INPUT, ACTION>