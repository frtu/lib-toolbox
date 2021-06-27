package com.github.frtu.sample.persistence.r2dbc.json

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("receiver", "subject", "content", "status")
data class EmailJsonDetail(
    var receiver: String? = null,
    var subject: String? = null,
    var content: String? = null,
    var status: String? = null
)