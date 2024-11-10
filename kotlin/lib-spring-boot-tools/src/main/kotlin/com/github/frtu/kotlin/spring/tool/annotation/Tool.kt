package com.github.frtu.kotlin.spring.tool.annotation

/**
 * Tool annotate an executable function
 *
 * @author Frédéric TU
 * @since 2.0.9
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Tool(
    /** Id of the tool */
    val id: String,
    /** Description that can be used by agent to decide which tool to use */
    val description: String,
)
