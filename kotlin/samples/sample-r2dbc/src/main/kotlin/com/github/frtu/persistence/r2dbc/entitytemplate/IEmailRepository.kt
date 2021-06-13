package com.github.frtu.persistence.r2dbc.entitytemplate

import com.github.frtu.persistence.r2dbc.Email
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface IEmailRepository {
    suspend fun findById(id: Long): Email

    suspend fun findAll(): Flow<Email>

    suspend fun findAll(searchParams: Map<String?, String?>, pageable: Pageable? = null): Flow<Email>
}