package com.github.frtu.serdes.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frtu.serdes.jackson.lifecycle.ObjectMapperHolder;

import java.util.Map;

import static com.github.frtu.serdes.jackson.lifecycle.ObjectMapperLifecycleManager.objectMapperLifecycleManager;

public class JacksonSerdesFactory {
    /**
     * Holder to access {@link ObjectMapper}
     *
     * @since 1.1.1
     */
    private static ObjectMapperHolder HOLDER_FOR_JSON_PARAMETER = objectMapperLifecycleManager().getObjectMapperHolder();

    /**
     * Holder to access {@link ObjectMapper}
     *
     * @since 1.1.1
     */
    private ObjectMapperHolder holderForLogCalls = objectMapperLifecycleManager().getObjectMapperHolder();


    private String getJson(Map map) {
        String data;
        try {
            data = holderForLogCalls.getObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            data = map.toString();
        }
        return data;
    }

    public static <V> JsonNode toJsonNode(V value) {
        JsonNode jsonNode = HOLDER_FOR_JSON_PARAMETER.getObjectMapper().convertValue(value, JsonNode.class);
        return jsonNode;
    }
}
