package com.github.frtu.workflow.temporal.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
annotation class ActivityImplementation(
    val taskQueue: String
)