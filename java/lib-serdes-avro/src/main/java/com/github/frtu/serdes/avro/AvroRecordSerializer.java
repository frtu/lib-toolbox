package com.github.frtu.serdes.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Avro record serializer.
 * <p>
 * NOTE : To use into Kafka, please import library : com.github.frtu.governance:library-serdes
 * </p>
 *
 * @param <T> The specific Avro class it is meant to serialize
 * @author frtu
 * @since 0.3.5
 */
public abstract class AvroRecordSerializer<T extends GenericContainer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroRecordSerializer.class);

    private boolean isFormatJson = false;

    protected AvroRecordSerializer() {
        this(false);
    }

    protected AvroRecordSerializer(boolean isFormatJson) {
        this.isFormatJson = isFormatJson;
    }

    protected abstract DatumWriter<T> buildDatumWriter(Schema schema);

    /**
     * @param record an Avro record
     * @return the according bytes
     * @throws IOException Serialization exception
     */
    public byte[] serialize(T record) throws IOException {
        return serialize(record, this.isFormatJson);
    }

    public byte[] serialize(T record, boolean isFormatJson) throws IOException {
        final Schema schema = record.getSchema();
        LOGGER.debug("Serialize record:{} schema:{}", record, schema);

        DatumWriter<T> writer = buildDatumWriter(schema);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Encoder encoder;
        if (isFormatJson) {
            encoder = EncoderFactory.get().jsonEncoder(schema, byteArrayOutputStream);
        } else {
            encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
        }
        writer.write(record, encoder);
        encoder.flush();
        byteArrayOutputStream.close();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        LOGGER.debug("Serialize successfully bytes:{}", bytes);
        return bytes;
    }

    public void close() {
        // nothing to do
    }
}
