package com.github.frtu.utils.io;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class PeekInputStreamWrapperTest {
    @Test
    void peek() throws IOException {
        //--------------------------------------
        // 1. Constructor only call once
        //--------------------------------------
        final String absolutePath = new ResourceHelper().getAbsolutePath("classpath:dummy-bean.json");
        final FileInputStream fileInputStream = new FileInputStream(absolutePath);
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        final PeekInputStreamWrapper inputStreamWrapper = new PeekInputStreamWrapper(fileInputStream);
        final String peekString = inputStreamWrapper.peekString();
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertThat(peekString.trim()).isEqualTo("{\"id\":\"id\",\"name\":\"name\"}");
    }
}