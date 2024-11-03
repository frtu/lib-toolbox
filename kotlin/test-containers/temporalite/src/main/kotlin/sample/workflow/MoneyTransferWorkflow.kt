package sample.workflow

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod
import sample.model.TransferInfo
import sample.model.TransferResult

@WorkflowInterface
interface MoneyTransferWorkflow {
    // The Workflow method is called by the initiator either via code or CLI.
    @WorkflowMethod
    fun transfer(transferInfo: TransferInfo): TransferResult

    companion object {
        const val TASK_QUEUE = "MONEY_TRANSFER_TASK_QUEUE"
    }
}