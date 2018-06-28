/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.service;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import com.foilen.login.exception.LoginException;
import com.foilen.smalltools.email.EmailBuilder;
import com.foilen.smalltools.email.EmailService;
import com.foilen.smalltools.exception.SmallToolsException;

public class LoginUserEmailServiceImpl implements LoginUserEmailService {

    private final static Logger logger = LoggerFactory.getLogger(LoginUserEmailServiceImpl.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;

    @Value("${login.fromEmail}")
    private String emailFrom;

    @Override
    public void sendEmail(String to, String subjectCode, String resourceHtmlTemplateFile, Map<String, Object> dataModel, Locale locale) {

        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setFrom(emailFrom);
        emailBuilder.addTo(to);
        emailBuilder.setSubject(messageSource.getMessage(subjectCode, null, locale));
        emailBuilder.setBodyHtmlFromFreemarker(resourceHtmlTemplateFile.replaceAll("%LANG%", locale.getLanguage()), dataModel);
        emailBuilder.addInlineAttachmentFromResource("logo", "/com/foilen/login/emails/logo.png");

        // Send email
        try {
            emailService.sendEmail(emailBuilder);
        } catch (SmallToolsException e) {
            logger.error("Could not send email", e.getCause());
            throw new LoginException("Could not send email", e.getCause());
        } catch (Exception e) {
            logger.error("Could not send email", e);
            throw new LoginException("Could not send email", e);
        }
    }
}
