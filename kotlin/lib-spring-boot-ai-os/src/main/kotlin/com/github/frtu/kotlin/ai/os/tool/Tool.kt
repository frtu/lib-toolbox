package com.github.frtu.kotlin.ai.os.tool

import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.action.management.ActionMetadata

/**
 * Tool is an abstraction for simple function and a complex agent containing
 *
 * - All the action metadata
 * - An execution function to call
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
interface Tool : ActionMetadata, GenericAction
