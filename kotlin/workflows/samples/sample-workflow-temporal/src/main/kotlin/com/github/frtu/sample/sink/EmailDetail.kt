package com.github.frtu.sample.sink

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("receiver", "subject", "content", "status")
data class EmailDetail(
    var receiver: String? = null,
    var subject: String? = null,
    var content: String? = null,
    var status: String? = null
)
