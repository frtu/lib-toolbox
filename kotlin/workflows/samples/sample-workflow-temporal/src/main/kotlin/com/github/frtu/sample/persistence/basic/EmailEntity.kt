package com.github.frtu.sample.persistence.basic

import com.github.frtu.sample.persistence.basic.EmailEntity.Companion.TABLE_NAME
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(TABLE_NAME)
data class EmailEntity(
    @Column("receiver")
    var receiver: String? = null,

    @Column("subject")
    var subject: String = "",

    @Column("content")
    var content: String = "",

    @Column("status")
    var status: STATUS = STATUS.INIT,

    @Id
    @Column("id")
    var id: String? = null,

//    @CreatedDate
//    @Column("creation_time")
//    val creationTime: LocalDateTime = LocalDateTime.now(),
//
//    @LastModifiedDate
//    @Column("update_time")
//    var updateTime: LocalDateTime = creationTime
) {
    companion object {
        const val TABLE_NAME = "email"
    }
}

enum class STATUS {
    INIT, SENT, ERROR, UNKNOWN
}
