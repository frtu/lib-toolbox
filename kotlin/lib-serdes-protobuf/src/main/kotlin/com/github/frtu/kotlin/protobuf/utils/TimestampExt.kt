package com.github.frtu.kotlin.protobuf.utils

import com.google.protobuf.Timestamp
import com.google.protobuf.util.Timestamps
import java.time.*

fun Long.toTimestamp(): Timestamp = Timestamps.fromMillis(this)

fun Instant.toTimestamp(): Timestamp = this.toEpochMilli().toTimestamp()
fun ZonedDateTime.toTimestamp(): Timestamp = this.toInstant().toTimestamp()
fun LocalDateTime.toTimestamp(offset: ZoneOffset = ZoneOffset.UTC): Timestamp = this.toInstant(offset).toTimestamp()

fun Timestamp.toEpochMilli(): Long = Timestamps.toMillis(this)
fun Timestamp.toInstant(): Instant = Instant.ofEpochSecond(this.seconds, this.nanos.toLong())
fun Timestamp.toZonedDateTime(zoneId: ZoneId = ZoneId.of("UTC")): ZonedDateTime = toInstant().atZone(zoneId)
fun Timestamp.toLocalDateTime(offset: ZoneOffset = ZoneOffset.UTC): LocalDateTime =
    LocalDateTime.ofInstant(toInstant(), offset)
