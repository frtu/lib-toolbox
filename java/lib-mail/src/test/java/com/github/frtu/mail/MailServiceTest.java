package com.github.frtu.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Slf4j
class MailServiceTest {
    @Test
    void splitEmailSingleEmail() {
        final MailService mailService = new MailService();
        final String[] emails = mailService.splitEmail("xxx@yyy.com");
        Arrays.stream(emails).forEach(email -> LOGGER.trace(email));
        assertArrayEquals(new String[]{"xxx@yyy.com"}, emails);
    }

    @Test
    void splitEmailArray() {
        final MailService mailService = new MailService();
        final String[] emails = mailService.splitEmail("xxx@yyy.com,zzz@yyy.com,aaa@yyy.com");
        Arrays.stream(emails).forEach(email -> LOGGER.trace(email));
        assertArrayEquals(new String[]{"xxx@yyy.com", "zzz@yyy.com", "aaa@yyy.com"}, emails);
    }
}