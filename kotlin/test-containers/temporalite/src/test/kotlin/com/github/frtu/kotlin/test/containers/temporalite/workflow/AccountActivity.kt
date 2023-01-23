package com.github.frtu.kotlin.test.containers.temporalite.workflow

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface AccountActivity {
    @ActivityMethod
    fun deposit(accountId: String, referenceId: String, amount: Double)

    @ActivityMethod
    fun withdraw(accountId: String, referenceId: String, amount: Double)
}