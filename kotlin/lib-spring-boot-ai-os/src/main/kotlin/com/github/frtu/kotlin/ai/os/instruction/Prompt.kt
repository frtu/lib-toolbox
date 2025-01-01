package com.github.frtu.kotlin.ai.os.instruction

import com.github.mustachejava.DefaultMustacheFactory

import com.github.mustachejava.MustacheFactory
import java.io.StringReader
import java.io.StringWriter

data class Prompt(
    val template: String,
    val name: String = template.take(100),
) {
    private val mustache = mustacheFactory.compile(StringReader(template), name)

    fun render(context: Map<String, Any>): String {
        val writer = StringWriter()
        mustache.execute(writer, context).flush()
        return writer.toString()
    }

    companion object {
        private var mustacheFactory: MustacheFactory = DefaultMustacheFactory()
    }
}