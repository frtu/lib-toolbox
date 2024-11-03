package sample.model

import com.github.frtu.kotlin.utils.data.ValueObject

@ValueObject
data class TransferResult(
    val status: String,
)
