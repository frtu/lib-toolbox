package com.github.frtu.persistence.r2dbc.query

import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.relational.core.query.Criteria

internal class PostgresJsonbQueryBuilderTest {
    private val logger = LoggerFactory.getLogger(PostgresJsonbQueryBuilderTest::class.java)

    @Test
    fun `Test pageable query`() {
        val query = PostgresJsonbQueryBuilder().query(Criteria.empty(), PageRequest.of(2, 2))
        logger.debug("offset=[${query.offset}] limit=[${query.limit}]")
        // Page 2, offset is equals to 4
        query.offset shouldBe 4
        query.limit shouldBe 2
    }

    @Test
    fun `Testing criteria mapping empty parameter`() {
        val criteria = PostgresJsonbQueryBuilder().criteria(mapOf())
        criteria shouldBe Criteria.empty()
    }

    @Test
    fun `Testing criteria mapping correctly parameters`() {
        val criteria = PostgresJsonbQueryBuilder().criteria(
            mapOf(
                "key1" to "value1",
                "key2" to "value2"
            )
        )
        val criteria1 = criteria.previous!!
        logger.debug("column=[${criteria1.column}] value=[${criteria1.value}]")
        criteria1.column.toString() shouldBe "data->>'key1'"
        criteria1.value shouldBe "value1"

        val criteria2 = criteria.group[0]
        logger.debug("column=[${criteria2.column}] value=[${criteria2.value}]")
        criteria2.column.toString() shouldBe "data->>'key2'"
        criteria2.value shouldBe "value2"
    }

    @Test
    fun `Testing denied list`() {
        val postgresJsonbQueryBuilder = PostgresJsonbQueryBuilder(deniedKeys = setOf("denied-key1", "denied-key2"))
        val criteria = postgresJsonbQueryBuilder.criteria(
            mapOf(
                "denied-key1" to "value2",
                "allowed-key1" to "value1",
                "denied-key2" to "value3",
            )
        )
        logger.debug("column=[${criteria.column}] value=[${criteria.value}]")
        criteria.column.toString() shouldBe "data->>'allowed-key1'"
        criteria.value shouldBe "value1"

        // There is ONLY one item left, since denied-key* should be removed
        criteria.previous.shouldBeNull()
    }


    @Test
    fun `Testing allowed list`() {
        val postgresJsonbQueryBuilder = PostgresJsonbQueryBuilder(allowedKeys = setOf("allowed-key1"))
        val criteria = postgresJsonbQueryBuilder.criteria(
            mapOf(
                "denied-key1" to "value2",
                "allowed-key1" to "value1",
                "denied-key2" to "value3",
            )
        )
        logger.debug("column=[${criteria.column}] value=[${criteria.value}]")
        criteria.column.toString() shouldBe "data->>'allowed-key1'"
        criteria.value shouldBe "value1"

        // There is ONLY one item left, since denied-key* should be removed
        criteria.previous.shouldBeNull()
    }
}