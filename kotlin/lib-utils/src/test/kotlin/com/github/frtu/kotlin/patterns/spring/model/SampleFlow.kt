package com.github.frtu.kotlin.patterns.spring.model

/**
 * A sample business flow defining : the Event, what is the ID and validation rules, how to execute request.
 * @author Frédéric TU
 * @since 1.1.4
 */
open class SampleFlow(
    val name: String,
    val shouldSucceed: Boolean = true
)