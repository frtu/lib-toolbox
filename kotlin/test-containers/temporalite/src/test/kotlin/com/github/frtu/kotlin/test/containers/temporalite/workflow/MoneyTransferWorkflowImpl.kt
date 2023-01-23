package com.github.frtu.kotlin.test.containers.temporalite.workflow

import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Workflow
import java.time.Duration

class MoneyTransferWorkflowImpl : MoneyTransferWorkflow {
    // RetryOptions specify how to automatically handle retries when Activities fail.
    private val retryOptions = RetryOptions {
        setInitialInterval(Duration.ofMillis(100))
        setMaximumInterval(Duration.ofSeconds(10))
        setBackoffCoefficient(2.0)
        setMaximumAttempts(10)
    }

    private val defaultActivityOptions = ActivityOptions {
        // ActivityOptions DSL
        setTaskQueue("TestQueue")
        setStartToCloseTimeout(Duration.ofSeconds(5)) // Timeout options specify when to automatically timeout Activities if the process is taking too long.
        // Temporal retries failures by default, this is simply an example.
        setRetryOptions(retryOptions)
    }

    // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
    private val perActivityMethodOptions: Map<String, ActivityOptions> = mapOf<String, ActivityOptions>(
        WITHDRAW to ActivityOptions { setHeartbeatTimeout(Duration.ofSeconds(5)) }
    )

    private val account =
        Workflow.newActivityStub(AccountActivity::class.java, defaultActivityOptions, perActivityMethodOptions)

    // The transfer method is the entry point to the Workflow.
    // Activity method executions can be orchestrated here or from within other Activity methods.
    override fun transfer(fromAccountId: String, toAccountId: String, referenceId: String, amount: Double) {
        account.withdraw(fromAccountId, referenceId, amount)
        account.deposit(toAccountId, referenceId, amount)
    }

    companion object {
        private const val WITHDRAW = "Withdraw"
    }
}