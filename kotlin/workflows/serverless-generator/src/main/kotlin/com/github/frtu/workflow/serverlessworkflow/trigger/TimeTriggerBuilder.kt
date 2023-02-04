package com.github.frtu.workflow.serverlessworkflow.trigger

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import com.github.frtu.workflow.serverlessworkflow.state.AbstractBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.TimeTrigger.Companion.TIME_TRIGGER_DEFAULT_NAME
import io.serverlessworkflow.api.cron.Cron
import io.serverlessworkflow.api.schedule.Schedule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZoneOffset

/**
 * Builder for time trigger
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
class TimeTriggerBuilder(
    name: String = TIME_TRIGGER_DEFAULT_NAME
) : AbstractBuilder<TimeTrigger>(TimeTrigger(), name) {
    /**
     * @see <a href="http://crontab.org/">cron expression syntax</a>
     */
    @DslBuilder
    var expression: String? = null

    @DslBuilder
    var validUntil: String? = null

    @DslBuilder
    var timezone: String = UTC

    override fun assignName(value: String?) {
        value?.let { model.name = it }
    }

    override fun build(): TimeTrigger {
        expression?.let {
            model.schedule.withCron(cron(it, validUntil))
        }
        model.schedule.timezone = this.timezone
        return model
    }

    @DslBuilder
    override var transition: String?
        get() = model.transition
        set(value) {
            assignTransition(value)
        }

    override fun assignTransition(value: String?) {
        value?.let { model.transition = it }
    }

    companion object {
        val UTC: String = ZoneOffset.UTC.toString()
    }
}

data class TimeTrigger(
    var name: String = TIME_TRIGGER_DEFAULT_NAME,
    val schedule: Schedule = Schedule(),
) : Trigger<TimeTrigger>(TriggerCategory.BY_TIME) {
    lateinit var transition: String

    override fun toResult(): List<TimeTrigger> = listOf(this)

    companion object {
        const val TIME_TRIGGER_DEFAULT_NAME = "TimeTrigger"
    }
}

@DslBuilder
fun byTime(
    name: String = TIME_TRIGGER_DEFAULT_NAME,
    options: TimeTriggerBuilder.() -> Unit = {}
): TimeTrigger =
    TimeTriggerBuilder(name).apply(options).build()

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
