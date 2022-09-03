package com.github.frtu.sample.serverless.workflow

import com.github.frtu.kotlin.utils.io.ResourceHelper

object WorkflowFileLoader {
    fun loadWorkflowDSLSwitch(shouldBeCorrect: Boolean = true) =
        if (shouldBeCorrect) ResourceHelper().readFromFile("classpath:workflows/switch/greeting.sw.json")!!
        else ResourceHelper().readFromFile("classpath:workflows/switch/greeting-INCORRECT.sw.json")!!

    fun loadWorkflowDSLEvent() =
        ResourceHelper().readFromFile("classpath:workflows/event/event-driven.sw.json")!!
}
