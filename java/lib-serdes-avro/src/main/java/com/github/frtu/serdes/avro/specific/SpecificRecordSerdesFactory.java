package com.github.frtu.serdes.avro.specific;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import com.github.frtu.serdes.avro.AvroRecordSerdesFactory;
import com.github.frtu.serdes.avro.AvroRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;

/**
 * AvroRecordSerdesFactory for Avro {@link SpecificRecord}.
 *
 * @param <T> The specific Avro class it is meant to serialize and deserialize
 * @since 0.3.5
 */
public class SpecificRecordSerdesFactory<T extends SpecificRecord> implements AvroRecordSerdesFactory<T> {

    private Schema schema;

    private boolean isFormatJson;

    public SpecificRecordSerdesFactory(Schema schema) {
        this(schema, false);
    }

    public SpecificRecordSerdesFactory(Schema schema, boolean isFormatJson) {
        this.schema = schema;
        this.isFormatJson = isFormatJson;
    }

    @Override
    public AvroRecordSerializer<T> buildSerializer() {
        return buildSerializer(this.isFormatJson);
    }

    @Override
    public AvroRecordSerializer<T> buildSerializer(boolean isFormatJson) {
        return new SpecificRecordSerializer<>(isFormatJson);
    }

    @Override
    public AvroRecordDeserializer<T> buildDeserializer() {
        return buildDeserializer(this.isFormatJson);
    }

    @Override
    public AvroRecordDeserializer<T> buildDeserializer(boolean isFormatJson) {
        return new SpecificRecordDeserializer(this.schema, isFormatJson);
    }
}
