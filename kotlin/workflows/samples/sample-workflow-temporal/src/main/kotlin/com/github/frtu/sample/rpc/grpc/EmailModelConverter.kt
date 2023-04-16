package com.github.frtu.sample.rpc.grpc

import com.github.frtu.sample.grpc.Email
import com.github.frtu.sample.grpc.EmailHistoryItem
import com.github.frtu.sample.persistence.basic.EmailEntity
import com.github.frtu.sample.persistence.basic.STATUS

fun EmailEntity.toEmailHistoryItem(): EmailHistoryItem = EmailHistoryItem.newBuilder()
    .setId(this.id.toString())
//    .setCreationTime(this.creationTime.toTimestamp())
//    .setUpdateTime(this.updateTime.toTimestamp())
    .setStatus(this.status.toString())
    .setData(toEmailData())
    .build()

fun EmailEntity.toEmailData(): Email = Email.newBuilder()
    .setReceiver(this.receiver!!)
    .setSubject(this.subject)
    .setContent(this.content)
    .build()

fun EmailHistoryItem.toEmailEntity() = EmailEntity(
    receiver = this.data.receiver,
    subject = this.data.subject,
    content = this.data.content,
    status = STATUS.valueOf(this.status),
)
