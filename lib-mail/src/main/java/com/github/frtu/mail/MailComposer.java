package com.github.frtu.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
public class MailComposer {
    private JavaMailSender mailSender;
    private MimeMessage mimeMessage;

    MailComposer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    MailComposer from(String from) {
        mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setFrom(new InternetAddress(from));
        } catch (MessagingException e) {
            raiseException("from", from, e);
        }
        return this;
    }

    public MailComposer to(String to) {
        try {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        } catch (MessagingException e) {
            raiseException("to", to, e);
        }
        return this;
    }

    public MailComposer subject(String subject) {
        try {
            mimeMessage.setSubject(subject);
        } catch (MessagingException e) {
            raiseException("subject", subject, e);
        }
        return this;
    }

    public MailComposer text(String body) {
        try {
            mimeMessage.setText(body);
        } catch (MessagingException e) {
            raiseException("text", body, e);
        }
        return this;
    }

    public void send() {
        mailSender.send(mimeMessage);
    }

    private void raiseException(String methodName, String arg, MessagingException e) {
        final String message = String.format("Wrong argument for method $s(%s)", methodName, arg);
        LOGGER.error(message, e);
        throw new IllegalArgumentException(message, e);
    }
}
