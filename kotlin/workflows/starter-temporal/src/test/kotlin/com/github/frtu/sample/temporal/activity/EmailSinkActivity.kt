package com.github.frtu.sample.temporal.activity

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface EmailSinkActivity {
    @ActivityMethod
    fun emit()
}

const val TASK_QUEUE_EMAIL = "TASK_QUEUE_EMAIL"
