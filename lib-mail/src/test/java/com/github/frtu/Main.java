package com.github.frtu;

import com.github.frtu.mail.JavaMailConfig;
import com.github.frtu.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
public class Main {
    public static void main(String[] args) throws MessagingException {
        LOGGER.info("Executing Main.main() with args.length:{}", args.length);

        ApplicationContext context = new AnnotationConfigApplicationContext(JavaMailConfig.class);
        final MailService mailService = context.getBean(MailService.class);

        mailService.sendText("rndfred@163.com", "Test lib", "Testing body");
    }
}

