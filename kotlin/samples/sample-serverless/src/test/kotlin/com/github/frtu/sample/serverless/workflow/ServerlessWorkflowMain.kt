package com.github.frtu.sample.serverless.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.utils.io.ResourceHelper
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

private val mapper: ObjectMapper = jacksonObjectMapper()

fun main() {
    val definition = ResourceHelper().readFromFile("workflow/event-driven/event-driven.sw.json")

    val serverlessWorkflow = ServerlessWorkflow.fromSource(definition)
    serverlessWorkflow.assertValidity()
    printJson(serverlessWorkflow)
}

private fun printJson(bean: Any) {
    println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean))
}