package com.github.frtu.kotlin.test.containers.temporalite.workflow

class AccountActivityImpl : AccountActivity {
    override fun withdraw(accountId: String, referenceId: String, amount: Double) {
        System.out.printf(
            "\nWithdrawing $%f from account %s. ReferenceId: %s\n",
            amount, accountId, referenceId
        )
    }

    override fun deposit(accountId: String, referenceId: String, amount: Double) {
        System.out.printf(
            "\nDepositing $%f into account %s. ReferenceId: %s\n",
            amount, accountId, referenceId
        )
        // Uncomment the following line to simulate an Activity error.
        // throw new RuntimeException("simulated");
    }
}