package com.github.frtu.serdes.avro;

import org.apache.avro.generic.GenericContainer;

/**
 * Factory for Avro Serializer and Deserializer.
 * <p>
 * Note : Usually they are used by two different applications to serializer and deserialize the same object.
 *
 * @param <T> The Avro class it is meant to serialize and deserialize
 * @since 0.3.5
 */
public interface AvroRecordSerdesFactory<T extends GenericContainer> {

    AvroRecordSerializer<T> buildSerializer();

    AvroRecordSerializer<T> buildSerializer(boolean isFormatJson);

    AvroRecordDeserializer<T> buildDeserializer();

    AvroRecordDeserializer<T> buildDeserializer(boolean isFormatJson);
}
