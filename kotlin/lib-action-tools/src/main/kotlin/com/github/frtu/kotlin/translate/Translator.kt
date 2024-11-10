package com.github.frtu.kotlin.translate

import com.github.frtu.kotlin.action.execution.TypedAction

/**
 * Transform from one type to another
 */
interface Translator<INPUT, OUTPUT> : TypedAction<INPUT, OUTPUT>