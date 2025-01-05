package sample.model

import com.github.frtu.kotlin.utils.data.ValueObject

@ValueObject
data class TransferInfo(
    val referenceId: String,
    val fromAccountId: String,
    val toAccountId: String,
    val amount: Double,
)
