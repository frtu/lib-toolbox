package com.github.frtu.workflow.serverlessworkflow.trigger

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import io.serverlessworkflow.api.cron.Cron
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Builder for time trigger
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
class CronBuilder(expression: String, validUntil: String? = null) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val model = Cron(expression)

    init {
        assignValidUntil(validUntil)
    }

    @DslBuilder
    var validUntil: String?
        get() = model.validUntil
        set(value) {
            assignValidUntil(value)
        }

    private fun assignValidUntil(value: String?) {
        logger.trace("Cron: validUntil={}", value)
        value?.let { model.withValidUntil(it) }
    }

    fun build(): Cron = model
}

/**
 * @see <a href="https://github.com/serverlessworkflow/specification/blob/main/specification.md#cron-definition">cron-definition</a>
 */
@DslBuilder
fun cron(expression: String, validUntil: String? = null, options: CronBuilder.() -> Unit = {}): Cron =
    CronBuilder(expression, validUntil).apply(options).build()
