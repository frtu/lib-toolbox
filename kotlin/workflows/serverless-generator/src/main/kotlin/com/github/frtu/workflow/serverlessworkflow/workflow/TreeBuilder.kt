package com.github.frtu.workflow.serverlessworkflow.workflow

import io.serverlessworkflow.api.end.End
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SwitchState
import io.serverlessworkflow.api.transitions.Transition
import org.slf4j.LoggerFactory

class TreeBuilder(
    private val treeNodeInterceptor: TreeNodeInterceptor<State>
    = object : TreeNodeInterceptor<State> {
        override fun hasNoChildren(parentNode: TreeNode<State>) {
            val state = parentNode.value
            if (state is DefaultState) {
                logger.debug("hasNoChildren:$parentNode => terminate")
                state.withEnd(End().withTerminate(true))
            }
        }
    }
) {
    fun buildTree(startName: String, vararg multiplelistNode: List<State>): TreeNode<State> {
        // Index
        val stateMap: MutableMap<String, State> = mutableMapOf()
        multiplelistNode.forEach { listNode ->
            stateMap.putAll(listNode.associateBy({ it.name }, { it }))
        }
        if (logger.isTraceEnabled) logger.trace("Received map of states:[${stateMap.keys.joinToString(",")}]")

        // Traverse
        var startState = getState(startName, stateMap)
        return traverseState(startState, stateMap)
    }

    private fun traverseState(stateName: String, stateMap: MutableMap<String, State>): TreeNode<State> =
        traverseState(getState(stateName, stateMap), stateMap)

    private fun traverseState(state: State, stateMap: MutableMap<String, State>): TreeNode<State> {
        val parentNode = TreeNode(state)
        logger.trace("Created TreeNode:${state.name}")
        if (state.end?.isTerminate != true) {
            val children = when (state) {
                is SwitchState -> {
                    val stateName = nextStateName(state.defaultCondition?.transition)
                    if (stateName != null) {
                        logger.trace("Find child from defaultCondition:$stateName")
                        mutableListOf(traverseState(stateName, stateMap)).apply {
                            for (dataCondition in state.dataConditions) {
                                val stateName = nextStateName(dataCondition?.transition)
                                if (stateName != null) {
                                    logger.trace("Find child from dataCondition:$stateName")
                                    add(traverseState(stateName, stateMap))
                                }
                            }
                        }
                    } else {
                        emptyList()
                    }
                }

                else -> {
                    if (state != null) {
                        val stateName = nextStateName(state?.transition)
                        if (stateName != null) {
                            logger.trace("Find child from transition:$stateName")
                            listOf(traverseState(stateName, stateMap))
                        } else {
                            emptyList()
                        }
                    } else emptyList()
                }
            }
            if (children.isNotEmpty()) {
                parentNode.children.addAll(children)
            } else {
                treeNodeInterceptor?.hasNoChildren(parentNode)
            }
        }
        return parentNode
    }

    private fun nextStateName(transition: Transition?): String? = transition?.nextState

    private fun getState(stateName: String, stateMap: MutableMap<String, State>) =
        stateMap[stateName] ?: throw IllegalArgumentException("State name=$stateName doesn't exist!")

    companion object {
        internal val logger = LoggerFactory.getLogger(this::class.java)
    }
}
