/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.foilen.smalltools.email.EmailService;
import com.foilen.smalltools.email.EmailServiceSpring;

@Configuration
public class MailSpringConfig {

    @Value("${login.mailHost}")
    private String mailHost;
    @Value("${login.mailPort}")
    private int mailPort;
    @Value("${login.mailUsername:#{null}}")
    private String mailUsername;
    @Value("${login.mailPassword:#{null}}")
    private String mailPassword;

    @Bean
    public EmailService emailService() {
        return new EmailServiceSpring();
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        return mailSender;
    }

}
