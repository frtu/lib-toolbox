package com.github.frtu.sample.rpc.grpc

import com.github.frtu.sample.grpc.SubscriptionEvent
import com.github.frtu.sample.grpc.workflows.ByWorkflowId
import com.github.frtu.sample.grpc.workflows.EmailDetail
import com.github.frtu.sample.grpc.workflows.WorkflowId
import com.github.frtu.sample.grpc.workflows.WorkflowServiceGrpcKt
import com.github.frtu.sample.temporal.staticwkf.starter.SubscriptionHandler
import com.google.protobuf.Empty
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.stereotype.Component

@Component
@GrpcService
class WorkflowServiceImpl(
    private val subscriptionHandler: SubscriptionHandler,
) : WorkflowServiceGrpcKt.WorkflowServiceCoroutineImplBase() {

    override suspend fun subscribe(request: SubscriptionEvent): WorkflowId =
        subscriptionHandler.handle(request.toSubscriptionEvent())
            .toWorkflowId()

    override suspend fun queryDetail(request: ByWorkflowId): EmailDetail {
        return subscriptionHandler.query(request.id)
            ?.toEmailDetailProto()
            ?: emptyEmailDetail
    }

    override suspend fun terminate(request: ByWorkflowId): Empty {
        subscriptionHandler.terminate(request.id)
        return Empty.getDefaultInstance()
    }

    override suspend fun fail(request: ByWorkflowId): Empty {
        subscriptionHandler.fail(request.id)
        return Empty.getDefaultInstance()
    }
}
