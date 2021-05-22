package com.github.frtu.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Slf4j
class MailServiceTest {
    @Test
    void splitEmailSingleEmail() {
        //--------------------------------------
        // 1. Prepare data & Init service
        //--------------------------------------
        final MailService mailService = new MailService();
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        final String[] emails = mailService.splitEmail("xxx@yyy.com");
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Arrays.stream(emails).forEach(email -> LOGGER.trace(email));
        assertArrayEquals(new String[]{"xxx@yyy.com"}, emails);
    }

    @Test
    void splitEmailArray() {
        //--------------------------------------
        // 1. Prepare data & Init service
        //--------------------------------------
        final MailService mailService = new MailService();
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        final String[] emails = mailService.splitEmail("xxx@yyy.com,zzz@yyy.com,aaa@yyy.com");
        Arrays.stream(emails).forEach(email -> LOGGER.trace(email));
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertArrayEquals(new String[]{"xxx@yyy.com", "zzz@yyy.com", "aaa@yyy.com"}, emails);
    }
}