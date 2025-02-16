package com.github.frtu.kotlin.ai.os.model

/**
 * Message class is a base unit from a Thread
 * @author frtu
 */
data class Message(
    /**
     * The role of the author of this message.
     */
    val role: Role,
    /**
     * The contents of the message.
     * **This is required for requests, and optional for responses**.
     */
    val simpleContent: String? = null,
    /**
     * The author's name of this message.
     * [name] is required if the role is `[ChatRole.Function], and it should be the name of the function whose response is
     * in the [content]. It May contain a-z, A-Z, 0-9, and underscores, with a maximum length of 64 characters.
     */
    val name: String? = null,
) {
    companion object {
        fun system(content: String): Message = Message(Role.SYSTEM, content)
        fun user(content: String): Message = Message(Role.USER, content)
        fun assistant(content: String): Message = Message(Role.ASSISTANT, content)
        fun function(functionName: String, content: String): Message = Message(Role.FUNCTION, content)
        fun tool(toolName: String, content: String): Message = Message(Role.TOOL, content)

        fun createMessage(
            role: Role,
            content: String? = null,
            name: String? = null,
//            functionCall: FunctionCall? = null,
        ) = Message(role, content, name)
    }
}