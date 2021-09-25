package com.github.frtu.utils.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceHelperTest {
    @Test
    void readFromFile() {
        final String jsonSampleFromString = new ResourceHelper().readFromFile("classpath:dummy-bean.json");
        assertThat(jsonSampleFromString.trim()).isEqualTo("{\"id\":\"id\",\"name\":\"name\"}");
    }
}