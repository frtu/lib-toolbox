package com.github.frtu.sample.temporal.activitydsl.model

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

private val mapper = ObjectMapper()

class WorkflowContext(var value: ObjectNode = mapper.createObjectNode()) {
    constructor(data: String?) : this(
        if (data.isNullOrBlank()) {
            mapper.createObjectNode()
        } else {
            mapper.readTree(data) as ObjectNode
        }
    )

    val customer: Customer?
        get() = try {
            mapper.readValue(value["customer"].toString(), Customer::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    fun addResults(result: ActResult) {
        val valueToTree: JsonNode = mapper.valueToTree(result)
        (value["results"] as ArrayNode).add(valueToTree)
    }

    fun valueToString(): String {
        return value.toPrettyString()
    }
}
