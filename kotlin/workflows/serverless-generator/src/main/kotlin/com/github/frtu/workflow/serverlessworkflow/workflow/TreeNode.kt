package com.github.frtu.workflow.serverlessworkflow.workflow

data class TreeNode<T>(
    val value: T,
    val children: MutableList<TreeNode<T>> = mutableListOf(),
)