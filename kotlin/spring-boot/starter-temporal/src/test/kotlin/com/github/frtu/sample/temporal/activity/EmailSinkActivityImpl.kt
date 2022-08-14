package com.github.frtu.sample.temporal.activity

import com.github.frtu.workflow.temporal.annotation.ActivityImplementation
import org.springframework.stereotype.Service

@Service
@ActivityImplementation(taskQueue = TASK_QUEUE_EMAIL)
class EmailSinkActivityImpl(
) : EmailSinkActivity {
    override fun emit() {
        println("Emit")
    }
}