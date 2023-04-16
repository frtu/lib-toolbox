package com.github.frtu.sample.rpc.grpc

import com.github.frtu.sample.grpc.workflows.WorkflowId
import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import java.util.UUID
import com.github.frtu.sample.grpc.SubscriptionEvent as SubscriptionEventProto
import com.github.frtu.sample.grpc.workflows.EmailDetail as EmailDetailProto

fun UUID.toWorkflowId(): WorkflowId = WorkflowId.newBuilder()
    .setId(this.toString())
    .build()

fun SubscriptionEventProto.toSubscriptionEvent(): SubscriptionEvent = SubscriptionEvent(
    id = UUID.fromString(this.id),
    type = this.type,
    data = this.data,
)

fun EmailDetail.toEmailDetailProto() = EmailDetailProto.newBuilder()
    .setReceiver(this.receiver)
    .setSubject(this.subject)
    .setContent(this.content)
    .setStatus(this.status)
    .build()

val emptyEmailDetail: EmailDetailProto = EmailDetailProto.getDefaultInstance()
