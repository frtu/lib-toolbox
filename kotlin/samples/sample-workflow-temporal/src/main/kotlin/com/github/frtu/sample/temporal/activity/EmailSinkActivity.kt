package com.github.frtu.sample.temporal.activity

import com.github.frtu.sample.sink.EmailDetail
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface EmailSinkActivity {
    @ActivityMethod
    fun emit(email: EmailDetail)
}

const val TASK_QUEUE_EMAIL = "TASK_QUEUE_EMAIL"
