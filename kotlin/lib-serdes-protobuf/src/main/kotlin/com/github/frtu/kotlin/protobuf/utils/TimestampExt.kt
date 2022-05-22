package com.github.frtu.kotlin.protobuf.utils

import com.google.protobuf.Timestamp
import com.google.protobuf.util.Timestamps
import java.time.Instant

fun Long.toTimestamp(): Timestamp = Timestamps.fromMillis(this)
fun Instant.toTimestamp(): Timestamp = this.toEpochMilli().toTimestamp()

fun Timestamp.toEpochMilli(): Long = Timestamps.toMillis(this)
