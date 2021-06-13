package com.github.frtu.persistence.r2dbc

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("email")
data class Email(
    @Column("receiver")
    var receiver: String? = null,

    @Column("subject")
    var subject: String? = null,

    @Column("content")
    var content: String? = null,

    @Column("status")
    var status: String? = null,

    @Id
    @Column("id")
    var id: Long? = null,

    @CreatedDate
    @Column("creation_time")
    val creationTime: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column("update_time")
    var updateTime: LocalDateTime = creationTime
) {
    companion object {
        const val TABLE_NAME = "email"
    }
}

enum class STATUS {
    INIT, SENT, ERROR
}