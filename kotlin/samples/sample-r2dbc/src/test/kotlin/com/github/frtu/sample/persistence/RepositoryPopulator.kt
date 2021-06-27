package com.github.frtu.sample.persistence

import com.github.frtu.persistence.r2dbc.config.PostgresR2dbcConfiguration
import com.github.frtu.sample.persistence.r2dbc.basic.Email
import com.github.frtu.sample.persistence.r2dbc.basic.STATUS
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.Instant
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
// DEPRECATED in spring-data:1.2.x
//import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager
//import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator
//import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
//import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
import java.util.*

@Configuration
@Import(PostgresR2dbcConfiguration::class)
class RepositoryPopulatorConfig {
    @Bean
    fun initDatabase(repository: CoroutineCrudRepository<Email, UUID>): CommandLineRunner {
        val receiverList = listOf("rndfred@gmail.com", "rndfred@hotmail.com")
        val statusList = STATUS.values().toList()

        val objectMapper = ObjectMapper()
        return CommandLineRunner { args: Array<String?>? ->
            runBlocking {
                // BUILD TIME
                var moment = Instant.now()

                // POPULATE DATA
//                val monthlySeconds = 2_635_200
//
//                val itemPerBucket = 320_000 * 4
                val totalPopulation = 100// itemPerBucket * 10
//                val increment = monthlySeconds / itemPerBucket
                for (i in 1..totalPopulation) {
                    var receiverEmail = pickOneBasedOnSeed(receiverList, i)
                    var status = pickOneBasedOnSeed(statusList, i)

                    repository.save(
                        Email(
                            receiverEmail, "Mail subject",
                            "Lorem ipsum dolor sit amet.", status.name
                        )
                    )
//                    moment = moment.plusSeconds(increment.toLong())

                    // Progress bar
                    if ((i % 10) == 0) {
                        print(".")
                    }
                }

                repository.findAll().toList(mutableListOf())
                    .forEach { email -> println(email) }
            }
        }
    }

    private fun <T> pickOneBasedOnSeed(list: List<T>, seed: Int): T {
        return list.get(seed % list.size)
    }
}

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
