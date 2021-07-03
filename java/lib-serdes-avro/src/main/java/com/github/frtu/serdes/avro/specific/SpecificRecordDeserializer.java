package com.github.frtu.serdes.avro.specific;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;

/**
 * An Avro record deserializer for POJO
 *
 * @param <T> The specific Avro class it is meant to deserialize
 * @author frtu
 * @since 0.3.5
 */
public class SpecificRecordDeserializer<T extends SpecificRecord> extends AvroRecordDeserializer<T> {

    public SpecificRecordDeserializer(T similarRecord) {
        this(similarRecord, false);
    }

    public SpecificRecordDeserializer(T similarRecord, boolean isFormatJson) {
        super(similarRecord.getSchema(), isFormatJson);
    }

    public SpecificRecordDeserializer(Schema schema) {
        super(schema);
    }

    public SpecificRecordDeserializer(Schema schema, boolean isFormatJson) {
        super(schema, isFormatJson);
    }

    @Override
    protected DatumReader<T> buildDatumReader() {
        return new SpecificDatumReader<>(getSchema());
    }
}
