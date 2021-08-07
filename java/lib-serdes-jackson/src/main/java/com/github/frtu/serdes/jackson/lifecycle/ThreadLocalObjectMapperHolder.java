package com.github.frtu.serdes.jackson.lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.frtu.serdes.jackson.lifecycle.ObjectMapperLifecycleManager.buildObjectMapper;

/**
 * Basic holder that always return the same {@link ObjectMapper}.
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
public class ThreadLocalObjectMapperHolder implements ObjectMapperHolder {
    // For more advanced version check com.fasterxml.jackson.core.util.ThreadLocalBufferManager
    private final ThreadLocal<ObjectMapper> OBJECT_MAPPER_THREAD_LOCAL = ThreadLocal.withInitial(() -> buildObjectMapper());

    @Override
    public ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER_THREAD_LOCAL.get();
    }
}
