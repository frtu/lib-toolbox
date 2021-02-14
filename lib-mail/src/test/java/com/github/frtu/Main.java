package com.github.frtu;

import com.github.frtu.mail.JavaMailConfig;
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
    public static void sendMail(JavaMailSender mailSender, String to, String subject, String body)
            throws MessagingException {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(new InternetAddress("rndfred@163.com"));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

        mimeMessage.setSubject(subject);
        mimeMessage.setText(body);
        mailSender.send(mimeMessage);
    }

    public static void main(String[] args) throws MessagingException {
        LOGGER.info("Executing Main.main() with args.length:{}", args.length);

        ApplicationContext context = new AnnotationConfigApplicationContext(JavaMailConfig.class);
        final JavaMailSender mailSender = context.getBean(JavaMailSender.class);

        sendMail(mailSender, "rndfred@163.com", "Test lib", "Testing body");
    }
}

