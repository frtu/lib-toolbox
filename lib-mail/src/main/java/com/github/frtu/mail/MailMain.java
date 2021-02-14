package com.github.frtu.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class MailMain {
    public static void main(String[] args) {
        LOGGER.info("Executing Main.main() with args.length:{}", args.length);
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage main : 'to' 'subject' 'body'");
        }

        ApplicationContext context = new AnnotationConfigApplicationContext(JavaMailConfig.class);
        final MailService mailService = context.getBean(MailService.class);

        mailService.sendText(args[0], args[1], args[2]);
    }
}

