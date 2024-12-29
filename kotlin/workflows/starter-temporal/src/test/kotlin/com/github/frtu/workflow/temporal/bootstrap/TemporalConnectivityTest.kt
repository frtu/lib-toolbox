package com.github.frtu.workflow.temporal.bootstrap

import com.github.frtu.sample.temporal.workflow.SubscriptionWorkflowImpl
import com.github.frtu.sample.temporal.workflow.TASK_QUEUE_SUBSCRIPTION
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TemporalConnectivityTest {
    private val temporalConnectivity: TemporalConnectivity =
        TemporalConnectivity("com.github.frtu.sample.temporal", AopHelper())

    @Test
    fun scanClasspathForWorkflow() {
        val scanClasspathForWorkflow = temporalConnectivity.scanClasspathForWorkflow()
        scanClasspathForWorkflow.isEmpty() shouldBe false
    }


    @Test
    fun getWorkflowTaskQueue() {
        val taskQueue = temporalConnectivity.getWorkflowTaskQueue(SubscriptionWorkflowImpl::class.java)
        taskQueue shouldBe TASK_QUEUE_SUBSCRIPTION
    }
}