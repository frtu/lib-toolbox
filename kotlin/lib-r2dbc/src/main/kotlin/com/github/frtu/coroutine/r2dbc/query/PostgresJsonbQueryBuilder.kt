package com.github.frtu.persistence.r2dbc.query

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query

class PostgresJsonbQueryBuilder(
    private val skipKeys: Set<String> = setOf(),
    private val idColumnName: String = "id",
    private val jsonbColumnName: String = "data"
) : IPostgresJsonbQueryBuilder {
    private val LOGGER: Logger = LoggerFactory.getLogger(PostgresJsonbQueryBuilder::class.java)

    override fun id(id: Any): Query = Query.query(Criteria.where(idColumnName).`is`(id))
        .apply { LOGGER.debug("""{"query_type":"id", "${idColumnName}":"${id}"""") }

    override fun query(criteriaMap: Map<String, String>, pageable: Pageable?): Query {
        val criteria = criteria(criteriaMap)
        return query(criteria, pageable)
    }

    override fun query(criteriaMap: Map<String, String>, offset: Long?, limit: Int?): Query {
        val criteria = criteria(criteriaMap)
        return query(criteria, offset, limit)
    }

    override fun query(criteria: Criteria, pageable: Pageable?): Query {
        var query = Query.query(criteria)
        if (pageable != null && pageable.isPaged) {
            LOGGER.debug("""{"offset":${pageable.offset}, "limit":${pageable.pageSize}, "sort":"${pageable.sort}"}""")
            query = query.with(pageable)
        }
        return query
    }

    override fun query(criteria: Criteria, offset: Long?, limit: Int?): Query {
        var query = Query.query(criteria)
        LOGGER.debug("""{"offset":${offset}, "limit":${limit}}""")
        offset ?. let { query = query.offset(offset) }
        limit ?. let { query = query.limit(limit) }
        return query
    }

    override fun criteria(criteriaMap: Map<String, String>): Criteria {
        val criteriaIterator = criteriaMap
            // skip empty key or value
            .filter { it.key != null && it.value != null }
            .filter { !skipKeys.contains(it.key) }
            .map { Criteria.where("${jsonbColumnName}->>'${it.key}'").`is`(it.value) }
            .asSequence().iterator()

        // Build fluent CriteriaDefinition
        var criteria: Criteria? = null
        if (criteriaIterator.hasNext()) {
            criteria = criteriaIterator.next()
            while (criteriaIterator.hasNext()) {
                criteria = criteria?.and(criteriaIterator.next())
            }
            LOGGER.debug("""{"query_type":"criteria", criteria":"${criteria}"""")
        }
        return criteria ?: Criteria.empty()
    }
}