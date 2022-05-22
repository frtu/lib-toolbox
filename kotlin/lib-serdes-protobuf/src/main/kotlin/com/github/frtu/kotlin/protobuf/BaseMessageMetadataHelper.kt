package com.github.frtu.kotlin.protobuf

import com.google.protobuf.DescriptorProtos.MessageOptions
import com.google.protobuf.Descriptors
import com.google.protobuf.GeneratedMessage
import java.util.*

open class BaseMessageMetadataHelper<T>(
    private val messageOptions: GeneratedMessage.GeneratedExtension<MessageOptions, T>,
) {
    fun getExtension(messageDescriptor: Descriptors.Descriptor?): Optional<T> {
        if (messageDescriptor == null) {
            return Optional.empty()
        }
        val options = messageDescriptor.options
        return Optional.of(options.getExtension(messageOptions))
    }

    fun hasExtension(messageDescriptor: Descriptors.Descriptor?): Boolean {
        return getExtension(messageDescriptor).isPresent
    }
}
