//package com.github.frtu.kotlin.ai.os.tool.agent
//
////import com.github.frtu.sample.ai.agents.Agent
////import com.github.frtu.sample.ai.agents.getTaskPrompt
////import com.github.frtu.sample.ai.agents.getRolePrompt
////import com.github.frtu.kotlin.ai.os.utils.SchemaGen.generateJsonSchema
//import com.github.frtu.sample.ai.agents.os.app.utils.SchemaGen.generateJsonSchema
//import jdk.internal.agent.Agent
//import kotlin.reflect.KClass
//import kotlin.reflect.KFunction
//import kotlin.reflect.jvm.jvmErasure
//
//object AgentCallGenerator {
//    fun generateSystemPrompt(functionToCall: KFunction<*>, owningClass: KClass<out Agent>): String = buildString {
//        append(owningClass.getRolePrompt())
//        append("\n\n")
//        append(functionToCall.getTaskPrompt())
//
//        val returnClass = functionToCall.returnType.jvmErasure.java
//        if (returnClass != Void::class.java) {
//            append("\n\n")
//            append(
//                """
//                The output should be formatted as a JSON instance that conforms to the JSON schema below.
//
//                As an example, for the schema {"properties": {"foo": {"title": "Foo", "description": "a list of strings", "type": "array", "items": {"type": "string"}}}, "required": ["foo"]}
//                the object {"foo": ["bar", "baz"]} is a well-formatted instance of the schema. The object {"properties": {"foo": ["bar", "baz"]}} is not well-formatted.
//
//                Here is the output schema:
//                ```
//                """.trimIndent()
//            )
//            append(generateJsonSchema(returnClass))
//            append(
//                """
//                ```
//                """.trimIndent()
//            )
//        }
//
//    }
//}
//
//
