package com.github.frtu.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailService {
    @Value("${mail.sender.from}")
    private String senderAddress;

    @Autowired
    private JavaMailSender mailSender;

    public void sendText(String to, String subject, String body) throws MessagingException {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(new InternetAddress(senderAddress));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

        mimeMessage.setSubject(subject);
        mimeMessage.setText(body);
        mailSender.send(mimeMessage);
    }
}
