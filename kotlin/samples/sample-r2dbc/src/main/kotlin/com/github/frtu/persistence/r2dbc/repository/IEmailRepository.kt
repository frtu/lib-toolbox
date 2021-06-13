package com.github.frtu.persistence.r2dbc.repository

import com.github.frtu.persistence.r2dbc.Email
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IEmailRepository : CoroutineCrudRepository<Email, UUID> {
}
