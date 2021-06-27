package com.github.frtu.sample.persistence.r2dbc.json.entitytemplate

import com.github.frtu.sample.persistence.r2dbc.json.EmailJson
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import java.util.*

interface IEmailJsonRepository {
    suspend fun findById(id: UUID): EmailJson

    suspend fun findAll(): Flow<EmailJson>

    suspend fun findAll(searchParams: Map<String?, String?>, pageable: Pageable? = null): Flow<EmailJson>
}