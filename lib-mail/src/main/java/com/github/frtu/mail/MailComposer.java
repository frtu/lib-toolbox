package com.github.frtu.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class MailComposer {
    private DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();

    private JavaMailSender mailSender;
    private MimeMessageHelper mimeMessageHelper;

    MailComposer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    MailComposer from(String from) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        } catch (MessagingException e) {
            throw new IllegalStateException("Cannot create MimeMessageHelper");
        }
        try {
            this.mimeMessageHelper.setFrom(new InternetAddress(from));
        } catch (MessagingException e) {
            raiseException("from", from, e);
        }
        return this;
    }

    public MailComposer to(String to) {
        try {
            mimeMessageHelper.addTo(new InternetAddress(to));
        } catch (MessagingException e) {
            raiseException("to", to, e);
        }
        return this;
    }

    public MailComposer subject(String subject) {
        try {
            mimeMessageHelper.setSubject(subject);
        } catch (MessagingException e) {
            raiseException("subject", subject, e);
        }
        return this;
    }

    public MailComposer text(String body) {
        try {
            mimeMessageHelper.setText(body);
        } catch (MessagingException e) {
            raiseException("text", body, e);
        }
        return this;
    }

    public MailComposer attachment(String... attachmentPath) {
        final List<Resource> resources = Arrays.stream(attachmentPath)
                .filter(Objects::nonNull)
                .filter(path -> path.contains(":"))
                .map(defaultResourceLoader::getResource)
                .collect(Collectors.toList());
        return attachment(resources.toArray(new Resource[resources.size()]));
    }

    public MailComposer attachment(Resource... resources) {
        if (resources != null) {
            for (Resource resource : resources) {
                final String attachmentFilename = resource.getFilename();
                LOGGER.debug("Adding attachment:{} description:{}", attachmentFilename, resource.getDescription());
                try {
                    if (resource.exists()) {
                        mimeMessageHelper.addAttachment(attachmentFilename, resource);
                    } else {
                        LOGGER.error("Attention : resource doesn't exist:{}", resource.getDescription());
                    }
                } catch (MessagingException e) {
                    raiseException("attachment", attachmentFilename, e);
                }
            }
        }
        return this;
    }

    public void send() {
        mailSender.send(mimeMessageHelper.getMimeMessage());
    }

    private void raiseException(String methodName, String arg, MessagingException e) {
        final String message = String.format("Wrong argument for method $s(%s)", methodName, arg);
        LOGGER.error(message, e);
        throw new IllegalArgumentException(message, e);
    }
}
