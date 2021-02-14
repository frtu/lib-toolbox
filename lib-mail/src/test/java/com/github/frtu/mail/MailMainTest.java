package com.github.frtu.mail;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class MailMainTest {
    @Test
    void copySubArray() {
        final String[] args = {"1", "2", "3", "4", "5"};
        final String[] subArray = MailMain.copySubArray(args, 3);
        Arrays.stream(subArray).forEach(System.out::println);
    }
}