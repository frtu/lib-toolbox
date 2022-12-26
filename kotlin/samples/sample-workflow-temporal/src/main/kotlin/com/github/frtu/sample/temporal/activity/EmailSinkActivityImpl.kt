package com.github.frtu.sample.temporal.activity

import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.sink.EmailSink
import com.github.frtu.workflow.temporal.annotation.ActivityImplementation
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
@ActivityImplementation(taskQueue = TASK_QUEUE_EMAIL)
class EmailSinkActivityImpl(
    private val emailSink: EmailSink,
) : EmailSinkActivity {
    override fun emit(emailDetail: EmailDetail) = runBlocking {
        if (emailDetail.content?.contains("ERROR") ?: false) {
            throw IllegalArgumentException("Exception requested by caller !")
        }
        emailSink.emit(emailDetail)
    }
}