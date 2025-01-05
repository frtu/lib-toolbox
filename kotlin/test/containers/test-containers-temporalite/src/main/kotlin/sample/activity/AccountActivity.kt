package sample.activity

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface AccountActivity {
    @ActivityMethod
    fun deposit(accountId: String, referenceId: String, amount: Double)

    @ActivityMethod
    fun withdraw(accountId: String, referenceId: String, amount: Double)
}