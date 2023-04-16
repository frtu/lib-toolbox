package com.github.frtu.sample.sink.database

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.key
import com.github.frtu.logs.core.StructuredLogger.message
import com.github.frtu.sample.domain.EmailCrudHandler
import com.github.frtu.sample.persistence.basic.EmailEntity
import com.github.frtu.sample.persistence.basic.EmailEntity.Companion.TABLE_NAME
import com.github.frtu.sample.persistence.basic.STATUS
import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.sink.EmailSink

class EmailDatabaseSink(
    private val emailCrudHandler: EmailCrudHandler,
) : EmailSink {
    override suspend fun emit(emailDetail: EmailDetail) {
        structuredLogger.info(key("table_name", TABLE_NAME), message("Storing event:$emailDetail"))
        emailCrudHandler.insertOne(emailDetail.toEntity())
    }

    private val structuredLogger = StructuredLogger.create(this::class.java)
}

fun EmailDetail.toEntity(): EmailEntity = EmailEntity(
    receiver = this.receiver,
    subject = this.subject ?: "empty_subject",
    content = this.content ?: "empty_content",
    status = this.status.toStatus(),
)

fun String?.toStatus(): STATUS = when (this) {
    "INIT" -> STATUS.INIT
    "SENT" -> STATUS.SENT
    "ERROR" -> STATUS.ERROR
    else -> STATUS.UNKNOWN
}
