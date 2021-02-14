package com.github.frtu.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@Slf4j
public class MailService {
    @Value("${mail.sender.from}")
    private String senderAddress;

    @Autowired
    private JavaMailSender mailSender;

    public MailComposer createMimeMessage() {
        final MailComposer mailComposer = new MailComposer(mailSender);
        return mailComposer.from(senderAddress);
    }

    public void sendText(String to, String subject, String body) {
        final MailComposer mailComposer = createMimeMessage();
        mailComposer.to(to)
                .subject(subject)
                .text(body)
                .send();
    }
}
