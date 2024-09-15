package com.github.frtu.kotlin.ai.os.tool.agent

import com.github.frtu.kotlin.ai.os.llm.Chat

class AgentBuilder(
    val chat: Chat,
) {
//    /**
//     * Creates an instance of [T] that utilizes our custom [InvocationHandler]
//     */
//    inline fun <reified T : Agent> createProxy(): T {
//        val service = T::class.java
//        val invocationHandler = object : InvocationHandler {
//            val conversation = Conversation()
//
//            override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
//                val nonNullArgs = args ?: arrayOf()
//                val lastArg = nonNullArgs.lastOrNull()
//                if(lastArg != null && lastArg is Continuation<*>) {
//                    throw OperationNotSupportedException("suspend method are not supported, please use normal function for your Agent")
//                }
//
//                // Create a systemDirective based on the current function called
//                val systemDirective = AgentCallGenerator.generateSystemPrompt(
//                    // Kotlin function
//                    method.kotlinFunction!!,
//                    // Declaring class that must extend Agent
//                    (method.declaringClass as Class<Agent>).kotlin,
//                )
//                // Retrieve the first parameter and transform to user message
//                val message = user(nonNullArgs[0].toString())
//
//                // Invoke Chat with the ongoing conversation
//                val result = try {
//                    with(conversation) {
//                        system(systemDirective)
//                        runBlocking {
//                            return@runBlocking chat.sendMessage(append(message)).content
//                        }
//                    } ?: throw IllegalStateException("Error")
//                } catch(e: InvocationTargetException) {
//                    throw e.targetException
//                }
//                return result
//            }
//        }
//        return Proxy.newProxyInstance(
//            service.classLoader,
//            arrayOf(service),
//            invocationHandler,
//        ) as T
//    }
}