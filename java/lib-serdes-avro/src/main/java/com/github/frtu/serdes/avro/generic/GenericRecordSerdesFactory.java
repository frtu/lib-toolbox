package com.github.frtu.serdes.avro.generic;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import com.github.frtu.serdes.avro.AvroRecordSerdesFactory;
import com.github.frtu.serdes.avro.AvroRecordSerializer;
import com.github.frtu.serdes.avro.json.JsonAvroSerdes;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

/**
 * AvroRecordSerdesFactory for Avro {@link GenericRecord}.
 *
 * @param <T> The generic Avro class it is meant to serialize and deserialize
 * @since 0.3.5
 */
public class GenericRecordSerdesFactory<T extends GenericRecord> implements AvroRecordSerdesFactory<T> {

    private Schema schema;

    private boolean isFormatJson;

    public GenericRecordSerdesFactory(Schema schema) {
        this(schema, false);
    }

    public GenericRecordSerdesFactory(Schema schema, boolean isFormatJson) {
        this.schema = schema;
        this.isFormatJson = isFormatJson;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public AvroRecordSerializer<T> buildSerializer() {
        return buildSerializer(this.isFormatJson);
    }

    @Override
    public AvroRecordSerializer<T> buildSerializer(boolean isFormatJson) {
        return new GenericRecordSerializer(isFormatJson);
    }

    @Override
    public AvroRecordDeserializer<T> buildDeserializer() {
        return buildDeserializer(this.isFormatJson);
    }

    @Override
    public AvroRecordDeserializer<T> buildDeserializer(boolean isFormatJson) {
        return new GenericRecordDeserializer(this.schema, isFormatJson);
    }

    public JsonAvroSerdes<T> buildConverter() {
        return new JsonAvroSerdes((GenericRecordDeserializer) buildDeserializer());
    }
}
