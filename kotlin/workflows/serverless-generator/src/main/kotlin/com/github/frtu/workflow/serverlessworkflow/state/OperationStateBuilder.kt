package com.github.frtu.workflow.serverlessworkflow.state

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import io.serverlessworkflow.api.actions.Action
import io.serverlessworkflow.api.functions.FunctionDefinition
import io.serverlessworkflow.api.functions.FunctionRef
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
@DslBuilder
class OperationStateBuilder(
    name: String? = null,
) : AbstractStateBuilder<OperationState>(OperationState(), OPERATION, name) {
    private val actions = mutableListOf<Action>()

    @DslBuilder
    operator fun Action.unaryPlus() {
        logger.trace("action: name={}", this.name)
        actions += this
    }

    override fun build(): OperationState =
        model.withActions(actions)

    companion object {
        fun buildFunctionDefinition(state: OperationState): List<FunctionDefinition> =
            if (state.actions.isNotEmpty()) {
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

@DslBuilder
fun operation(name: String? = null, options: OperationStateBuilder.() -> Unit = {}): OperationState =
    OperationStateBuilder(name).apply(options).build()

data class Call(val functionRefName: String, val name: String? = null) {
    constructor(function: KFunction<*>, name: String? = null) : this(generateFunctionName(function), name)
}

@DslBuilder
fun call(function: KFunction<*>, name: String? = null): Call = Call(function, name)

@DslBuilder
fun call(functionRefName: String, name: String? = null): Call = Call(functionRefName, name)

@DslBuilder
infix fun Call.using(options: ArgumentsBuilder.() -> Unit): Action =
    Action().withName(name)
        .withFunctionRef(
            FunctionRef()
                .withRefName(functionRefName)
                .withArguments(ArgumentsBuilder().apply(options).build())
        )

private fun generateFunctionName(function: KFunction<*>) = function.name

@DslBuilder
class ArgumentsBuilder {
    private val arguments = mutableMapOf<String, String>()

    @DslBuilder
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
