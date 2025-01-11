package com.github.frtu.kotlin.ai.os.instruction

import com.github.mustachejava.DefaultMustacheFactory

import com.github.mustachejava.MustacheFactory
import java.io.StringReader
import java.io.StringWriter

/**
 * A prompt template with an unique ID across application.
 * @since 2.0.14
 */
data class PromptTemplate(
    /** A unique ID across the application (make sure to avoid collision) */
    val id: String,
    /** Template with variable in double curly bracket (mustache loop and other instructions are supported) */
    val template: String,
    /** Description of the purpose of this prompt */
    val description: String? = null,
) {
    private val mustache = mustacheFactory.compile(StringReader(template), id)

    fun format(input: Map<String, Any>): String {
        val writer = StringWriter()
        mustache.execute(writer, input).flush()
        return writer.toString()
    }

    companion object {
        private var mustacheFactory: MustacheFactory = DefaultMustacheFactory()
    }
}