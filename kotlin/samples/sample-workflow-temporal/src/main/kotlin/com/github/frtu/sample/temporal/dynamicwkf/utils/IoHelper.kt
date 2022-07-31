package com.github.frtu.sample.temporal.dynamicwkf.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.frtu.sample.temporal.activitydsl.DslActivities
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files

object IoHelper {
    /** Read file and return contents as string  */
    @Throws(IOException::class)
    fun getFileAsString(fileName: String): String {
        val file = File(DslActivities::class.java.classLoader.getResource(fileName).file)
        return String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8)
    }

    @Throws(Exception::class)
    fun getSampleWorkflowInput(fileName: String): JsonNode {
        val workflowDataInput = getFileAsString(fileName)
        val objectMapper = ObjectMapper()
        return objectMapper.readTree(workflowDataInput)
    }
}