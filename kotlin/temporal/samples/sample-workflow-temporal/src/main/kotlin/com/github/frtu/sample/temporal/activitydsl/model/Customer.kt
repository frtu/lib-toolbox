package com.github.frtu.sample.temporal.activitydsl.model

data class Customer(
    var name: String? = null,
    var age: Int = 0,
    var transactions: List<Int>? = null,
)
