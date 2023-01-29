package com.github.frtu.workflow.serverlessworkflow.state

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.serverlessworkflow.api.actions.Action
import io.serverlessworkflow.api.functions.FunctionDefinition
import io.serverlessworkflow.api.functions.FunctionRef
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState.Type.OPERATION
import io.serverlessworkflow.api.states.OperationState
import org.slf4j.LoggerFactory
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * Builder for operation state
 *
 * @author frtu
 * @since 1.2.5
 */
class OperationStateBuilder(
    name: String? = null,
) : AbstractStateBuilder<OperationState>(OperationState(), OPERATION, name) {
    private val actions = mutableListOf<Action>()

    operator fun Action.unaryPlus() {
        logger.trace("action: name={}", this.name)
        actions += this
    }

    override fun build(): OperationState =
        model.withActions(actions)

    companion object {
        fun buildFunctionDefinition(state: State): List<FunctionDefinition> =
            if (state is OperationState && state.actions.isNotEmpty()) {
                state.actions.map {
                    val refName = it.functionRef.refName
                    logger.trace("{}: name={}", "Function", refName)
                    FunctionDefinition()
                        .withName(refName)
                        .withType(FunctionDefinition.Type.CUSTOM)
                }
            } else {
                emptyList()
            }

        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}

fun operation(name: String? = null, options: OperationStateBuilder.() -> Unit = {}): OperationState =
    OperationStateBuilder(name).apply(options).build()

data class Call(val function: KFunction<*>, val name: String? = null)

fun call(function: KFunction<*>, name: String? = null): Call = Call(function, name)

infix fun Call.using(options: ArgumentsBuilder.() -> Unit): Action =
    Action().withName(name)
        .withFunctionRef(
            FunctionRef()
                .withRefName(this.function.name)
                .withArguments(ArgumentsBuilder().apply(options).build())
        )

class ArgumentsBuilder {
    private val arguments = mutableMapOf<String, String>()

    infix fun KProperty<*>.with(expression: String) {
        arguments[this.name] = expression
    }

    fun build(): JsonNode = objectMapper.createObjectNode().apply {
        arguments.map { (key, value) ->
            this.put(key, value)
        }
    }
}

val objectMapper: ObjectMapper = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
