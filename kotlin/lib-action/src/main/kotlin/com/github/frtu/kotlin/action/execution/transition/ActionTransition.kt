package com.github.frtu.kotlin.action.execution.transition

import com.github.frtu.kotlin.action.execution.Action
import com.github.frtu.kotlin.action.management.ActionMetadata

/**
 * An action that transition to another action
 */
interface ActionTransition<INPUT, ACTION : ActionMetadata> : ActionMetadata, Action<INPUT, ACTION>