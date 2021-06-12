package com.github.frtu.persistence.r2dbc.query

import org.springframework.data.domain.Pageable
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query

interface IPostgresJsonbQueryBuilder {
    fun id(id: Any): Query

    fun criteria(criteriaMap: Map<String, String>): Criteria

    fun query(criteria: Criteria, pageable: Pageable? = null): Query
    fun query(criteria: Criteria, offset: Long?, limit: Int?): Query

    fun query(criteriaMap: Map<String, String>, pageable: Pageable? = null): Query
    fun query(criteriaMap: Map<String, String>, offset: Long?, limit: Int?): Query
}