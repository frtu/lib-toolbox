package com.github.frtu.sample.temporal.staticwkf.starter

import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.RpcLogger.responseBody
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.flow
import com.github.frtu.logs.core.StructuredLogger.flowId
import com.github.frtu.logs.core.StructuredLogger.key
import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import com.github.frtu.sample.temporal.staticwkf.workflow.AcknowledgeSignal
import com.github.frtu.sample.temporal.staticwkf.workflow.Status
import com.github.frtu.sample.temporal.staticwkf.workflow.SubscriptionWorkflow
import com.github.frtu.sample.temporal.staticwkf.workflow.TASK_QUEUE_SUBSCRIPTION
import io.temporal.api.enums.v1.WorkflowIdReusePolicy
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class SubscriptionHandlerImpl(
    private val client: WorkflowClient,
) : SubscriptionHandler {

    override fun handle(subscriptionEvent: SubscriptionEvent): UUID {
        val id = subscriptionEvent.id
        structuredLogger.info(flowId(id), flow("START"), requestBody(subscriptionEvent))

        val workflow: SubscriptionWorkflow = client.newWorkflowStub(
            SubscriptionWorkflow::class.java,
            WorkflowOptions.newBuilder()
                .setTaskQueue(TASK_QUEUE_SUBSCRIPTION)
                .setWorkflowId("SubscriptionWorkflow-$id")
                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE_FAILED_ONLY)
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .build()
        )
        val we = WorkflowClient.start(workflow::start, subscriptionEvent)

        structuredLogger.info(flowId(id), key("workflow_id", we.workflowId), key("run_id", we.runId))
        return id
    }

    override fun query(workflowId: String): EmailDetail? {
        structuredLogger.info(flowId(workflowId), flow("QUERY"))

        val subscriptionWorkflow = client.newWorkflowStub(
            SubscriptionWorkflow::class.java,
            workflowId,
        )
        val result = subscriptionWorkflow.queryEmailDetail()
        structuredLogger.info(flowId(workflowId), flow("QUERY"), responseBody(result))
        return result
    }

    override fun terminate(workflowId: String) = acknowledge(workflowId, Status.COMPLETED)

    override fun fail(workflowId: String) = acknowledge(workflowId, Status.FAILED)

    private fun acknowledge(workflowId: String, status: Status) {
        structuredLogger.info(flowId(workflowId), flow("ACK"), requestBody(status))
        val subscriptionWorkflow = client.newWorkflowStub(
            SubscriptionWorkflow::class.java,
            workflowId,
        )
        subscriptionWorkflow.acknowledge(AcknowledgeSignal(status))
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}
