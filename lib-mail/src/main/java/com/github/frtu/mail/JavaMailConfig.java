package com.github.frtu.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
@PropertySource("classpath:application.properties")
public class JavaMailConfig {
    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.smtp.socketFactory.class:}")
    private String socketFactory;

    @Value("${spring.mail.protocol:smtp}")
    private String protocol;

    @Value("${spring.mail.default-encoding:UTF-8}")
    private String defaultEncoding;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout:5000}")
    private String connectiontimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout:5000}")
    private String timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout:5000}")
    private String writetimeout;

    @Value("${spring.mail.properties.mail.smtp.ssl.trust:}")
    private String sslTrust;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String starttlsRequired;

    @Value("${spring.mail.debug:false}")
    private String mailDebug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        LOGGER.debug("Creating JavaMailSender using host:{} port:{}", host, port);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding(defaultEncoding);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.connectiontimeout", connectiontimeout);
        properties.put("mail.smtp.writetimeout", writetimeout);

        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        if (!sslTrust.isEmpty()) {
            properties.put("mail.smtp.ssl.trust", sslTrust);
        }
        if (!socketFactory.isEmpty()) {
            properties.put("mail.smtp.socketFactory.class", socketFactory);
        }
        properties.put("mail.debug", mailDebug);
        LOGGER.debug("Init using properties:{}", properties);

        return mailSender;
    }
}
