package com.github.frtu.kotlin.ai.os.transition

import com.github.frtu.kotlin.action.execution.transition.ActionTransition
import com.github.frtu.kotlin.action.management.ActionMetadata

/**
 * Handoff is a Tool like any other that allow to pass action to another Tool
 */
interface Handoff<INPUT, ACTION : ActionMetadata> : ActionTransition<INPUT, ACTION>