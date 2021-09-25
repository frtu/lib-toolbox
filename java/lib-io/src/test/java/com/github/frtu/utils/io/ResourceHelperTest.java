package com.github.frtu.utils.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceHelperTest {
    @Test
    void readFromFile() {
        //--------------------------------------
        // 1. Execute
        //--------------------------------------
        final String jsonSampleFromString = new ResourceHelper().readFromFile("classpath:dummy-bean.json");
        //--------------------------------------
        // 2. Validate
        //--------------------------------------
        assertThat(jsonSampleFromString.trim()).isEqualTo("{\"id\":\"id\",\"name\":\"name\"}");
    }
}