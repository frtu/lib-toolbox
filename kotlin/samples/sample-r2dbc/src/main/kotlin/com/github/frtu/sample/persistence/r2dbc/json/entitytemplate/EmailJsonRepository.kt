package com.github.frtu.sample.persistence.r2dbc.json.entitytemplate

import com.github.frtu.persistence.exception.DataNotExist
import com.github.frtu.persistence.r2dbc.query.IPostgresJsonbQueryBuilder
import com.github.frtu.persistence.r2dbc.query.PostgresJsonbQueryBuilder
import com.github.frtu.sample.persistence.r2dbc.json.EmailJson
import com.github.frtu.sample.persistence.r2dbc.json.EmailJsonDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EmailJsonRepository(private val template: R2dbcEntityTemplate) : IEmailJsonRepository {
    private val queryBuilder: IPostgresJsonbQueryBuilder = PostgresJsonbQueryBuilder(setOf("page", "size"))

    override suspend fun findAll(): Flow<EmailJson> = template.select(EmailJson::class.java)
        .all().asFlow()

    override suspend fun findAll(searchParams: Map<String?, String?>, pageable: Pageable?): Flow<EmailJson> {
        logger.debug("""{"query_type":"criteria", "criteria":"${searchParams}", "limit":${pageable?.pageSize}, "offset":${pageable?.offset}}""")
        return template
            .select(EmailJson::class.java)
            .matching(queryBuilder.query(searchParams, pageable))
            .all().asFlow()
    }

    override suspend fun findById(id: UUID): EmailJson {
        logger.debug("""{"query_type":"id", "id":"${id}"}""")
        return template
            .selectOne(
                queryBuilder.id(id), EmailJson::class.java
            ).awaitFirstOrNull() ?: throw DataNotExist(id.toString())
    }

    suspend fun update(id: UUID, emailJsonDetail: EmailJsonDetail): UUID? = template
        .update(
            queryBuilder.id(id),
            Update.update("data->>'status'", emailJsonDetail.status),
            EmailJson::class.java
        )
        .map { id }
        .awaitFirstOrNull() ?: throw DataNotExist(id.toString())

    suspend fun save(emailJsonDetail: EmailJsonDetail): UUID? = this.save(EmailJson(emailJsonDetail, UUID.randomUUID()))

    suspend fun save(email: EmailJson): UUID? = template
        .insert(EmailJson::class.java)
        .using(email)
        .map { email.identity }
        .awaitFirstOrNull()

    suspend fun deleteById(id: UUID): Int? = template
        .delete(
            queryBuilder.id(id), EmailJson::class.java
        ).awaitFirstOrNull() ?: throw DataNotExist(id.toString())

    internal val logger = LoggerFactory.getLogger(this::class.java)
}