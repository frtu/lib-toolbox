package com.github.frtu.serdes.avro;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Avro record deserializer.
 * <p>
 * NOTE : To use into Kafka, please import library : com.github.frtu.governance:library-serdes
 * </p>
 *
 * @param <T> The specific Avro class it is meant to deserialize
 * @author frtu
 * @since 0.3.5
 */
public abstract class AvroRecordDeserializer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroRecordDeserializer.class);

    private Schema schema;

    private boolean isFormatJson = false;

    protected AvroRecordDeserializer(Schema schema) {
        this(schema, false);
    }

    protected AvroRecordDeserializer(Schema schema, boolean isFormatJson) {
        this.schema = schema;
        this.isFormatJson = isFormatJson;
    }

    public Schema getSchema() {
        return schema;
    }

    protected abstract DatumReader<T> buildDatumReader();

    public T deserialize(ByteBuffer byteBuffer) throws IOException {
        return deserialize(byteBuffer.array());
    }

    public T deserialize(byte[] bytes) throws IOException {
        return deserialize(bytes, this.isFormatJson);
    }

    public T deserialize(byte[] bytes, boolean isFormatJson) throws IOException {
        LOGGER.debug("Deserialize bytes:{}", bytes);
        DatumReader<T> datumReader = buildDatumReader();
        Decoder decoder;
        if (isFormatJson) {
            decoder = DecoderFactory.get().jsonDecoder(this.schema, new ByteArrayInputStream(bytes));
        } else {
            decoder = DecoderFactory.get().binaryDecoder(bytes, null);
        }
        T record = datumReader.read(null, decoder);
        LOGGER.debug("Deserialize successful:{}", record);
        return record;
    }

    public void close() {
        // nothing to do
    }
}
