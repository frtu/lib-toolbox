package com.github.frtu.kotlin.action.management

/**
 * Unique Id with hyphen for an action, readable by human or LLM
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
data class ActionId(
    /** Normalized [a-zA-Z0-9-#()] - recommendation to use kebab-case */
    val value: String,
)
