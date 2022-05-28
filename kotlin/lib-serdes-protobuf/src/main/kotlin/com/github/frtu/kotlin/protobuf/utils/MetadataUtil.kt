package com.github.frtu.kotlin.protobuf.utils

import com.google.protobuf.Descriptors

object MetadataUtil {
    fun buildFieldsMap(descriptor: Descriptors.Descriptor): Map<String, Descriptors.FieldDescriptor> =
        descriptor.fields.map { it.name to it }.toMap()
}