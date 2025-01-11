package com.github.frtu.kotlin.ai.os.instruction

import com.github.mustachejava.DefaultMustacheFactory

import com.github.mustachejava.MustacheFactory
import java.io.StringReader
import java.io.StringWriter

/**
 * A prompt template with an unique ID across application.
 */
data class Prompt(
    /** A unique ID across the application (make sure to avoid collision) */
    val id: String,
    /** Template with variable in double curly bracket (mustache loop and other instructions are supported) */
    val template: String,
) {
    private val mustache = mustacheFactory.compile(StringReader(template), id)

    fun render(context: Map<String, Any>): String {
        val writer = StringWriter()
        mustache.execute(writer, context).flush()
        return writer.toString()
    }

    companion object {
        private var mustacheFactory: MustacheFactory = DefaultMustacheFactory()
    }
}