package com.github.frtu.workflow.temporal.bootstrap

import com.github.frtu.sample.temporal.workflow.SubscriptionWorkflowImpl
import com.github.frtu.sample.temporal.workflow.TASK_QUEUE_SUBSCRIPTION
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TemporalConnectivityTest {
    private val temporalConnectivity: TemporalConnectivity =
        TemporalConnectivity("com.github.frtu.sample.temporal", AopHelper())

    @Test
    fun scanClasspathForWorkflow() {
        val scanClasspathForWorkflow = temporalConnectivity.scanClasspathForWorkflow()
        assertThat(scanClasspathForWorkflow).isNotEmpty
    }


    @Test
    fun getWorkflowTaskQueue() {
        val taskQueue = temporalConnectivity.getWorkflowTaskQueue(SubscriptionWorkflowImpl::class.java)
        assertThat(taskQueue).isEqualTo(TASK_QUEUE_SUBSCRIPTION)
    }
}