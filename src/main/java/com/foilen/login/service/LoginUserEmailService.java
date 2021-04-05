/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.service;

import java.util.Locale;
import java.util.Map;

/**
 * Services to send emails to users.
 */
public interface LoginUserEmailService {
    void sendEmail(String to, String subject, String resourceHtmlTemplateFile, Map<String, Object> dataModel, Locale locale);
}
