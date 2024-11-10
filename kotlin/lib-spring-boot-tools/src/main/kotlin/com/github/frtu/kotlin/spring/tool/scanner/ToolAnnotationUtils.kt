package com.github.frtu.kotlin.spring.tool.scanner

import com.github.frtu.kotlin.spring.tool.annotation.Tool
import com.github.frtu.kotlin.tool.StructuredTool
import com.github.frtu.kotlin.tool.StructuredToolExecuter
import java.lang.reflect.Method
import org.springframework.core.annotation.AnnotationUtils

/**
 * Allow `ToolAnnotationUtils` to build `StructuredTool<INPUT, OUTPUT>` from `java.lang.reflect.Method`
 *
 * @author Frédéric TU
 * @since 2.0.9
 */
object ToolAnnotationUtils

fun <INPUT, OUTPUT> Method.toTool(
    target: Any,
): StructuredTool<INPUT, OUTPUT> {
    val toolAnnotation = AnnotationUtils.getAnnotation(this, Tool::class.java)
        ?: throw IllegalStateException("Current method doesn't have any @Tool annotation")
    return StructuredToolExecuter.create(
        id = toolAnnotation.id,
        description = toolAnnotation.description,
        executerMethod = this,
        targetObject = target,
    )
}