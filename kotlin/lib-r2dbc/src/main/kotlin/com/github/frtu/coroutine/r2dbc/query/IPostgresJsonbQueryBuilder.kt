package com.github.frtu.coroutine.r2dbc.query

import org.springframework.data.domain.Pageable
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query

/**
 * Builder for {@link Query} and {@link Criteria} of spring data.
 * Help to create query for id or Criteria using Map<String, String>
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
interface IPostgresJsonbQueryBuilder {
    /**
     * Allow to create a Query for ID of the desired Entity
     * @param id ID from the type of the entity
     * @param <T> Type for id entity (UUID, Long, ..)
     */
    fun <T : Any> id(id: T): Query

    fun criteria(criteriaMap: Map<String?, String?>): Criteria

    fun query(criteria: Criteria, pageable: Pageable? = null): Query
    fun query(criteria: Criteria, offset: Long?, limit: Int?): Query

    fun query(criteriaMap: Map<String?, String?>, pageable: Pageable? = null): Query
    fun query(criteriaMap: Map<String?, String?>, offset: Long?, limit: Int?): Query
}