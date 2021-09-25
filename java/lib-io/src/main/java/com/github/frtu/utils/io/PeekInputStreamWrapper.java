package com.github.frtu.utils.io;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Encapsulate an InputStream that cannot be read twice, to allow read the data multiple times.
 *
 * @author Frédéric TU
 * @since 1.1.3
 */
public class PeekInputStreamWrapper {
    private InputStream inputStream;
    private byte[] content;

    public PeekInputStreamWrapper(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        if (content == null) {
            return inputStream;
        } else {
            // Return a wrapped byte[] if previously read successfully
            return new ByteArrayInputStream(content);
        }
    }

    /**
     * Peek the data from inputStream and return byte[]
     *
     * @return may return null if read has issue
     */
    public byte[] peek() {
        if (content == null) {
            try {
                val byteArrayOutputStream = new ByteArrayOutputStream();
                val buffer = new byte[1024];
                for (int length; (length = inputStream.read(buffer)) != -1; ) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                content = byteArrayOutputStream.toByteArray();
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            }
        }
        return content;
    }

    /**
     * Peek the data from inputStream and return String
     *
     * @return may return null if read has issue
     */
    public String peekString() {
        return peekString(StandardCharsets.UTF_8);
    }

    /**
     * Peek the data from inputStream and return String
     *
     * @param charset the charset to be used for byte[] content
     * @return may return null if read has issue
     */
    public String peekString(Charset charset) {
        final byte[] peek = peek();
        return (peek != null) ? new String(peek, charset) : null;
    }

    private final static Logger logger = LoggerFactory.getLogger(PeekInputStreamWrapper.class);
}
