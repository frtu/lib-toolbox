package com.github.frtu.workflow.serverlessworkflow.workflow

interface TreeNodeInterceptor<T> {
    fun hasNoChildren(parentNode: TreeNode<T>)
}