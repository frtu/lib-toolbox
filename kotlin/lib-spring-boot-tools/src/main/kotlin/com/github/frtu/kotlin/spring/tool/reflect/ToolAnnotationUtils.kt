package com.github.frtu.kotlin.spring.tool.reflect

import com.github.frtu.kotlin.spring.tool.annotation.Tool
import com.github.frtu.kotlin.tool.StructuredToolExecuter
import java.lang.reflect.Method
import org.springframework.core.annotation.AnnotationUtils

object ToolAnnotationUtils

fun <INPUT, OUTPUT> Method.toTool(
    target: Any,
): com.github.frtu.kotlin.tool.Tool {
    val toolAnnotation = AnnotationUtils.getAnnotation(this, Tool::class.java)
        ?: throw IllegalStateException("Current method doesn't have any @Tool annotation")
    return StructuredToolExecuter.create<INPUT, OUTPUT>(
        id = toolAnnotation.id,
        description = toolAnnotation.description,
        executerMethod = this,
        targetObject = target,
    )
}