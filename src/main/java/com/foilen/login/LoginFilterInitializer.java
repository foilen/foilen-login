/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.security.web.csrf.CsrfFilter;

import com.foilen.smalltools.spring.security.CookiesGeneratedCsrfTokenRepository;

/**
 * Add some filters used in the Login application.
 */
public class LoginFilterInitializer implements ServletContextInitializer {

    @Value("${login.csrfSalt}")
    private String csrfSalt;
    @Value("${login.cookieUserName}")
    private String cookieUserName;
    @Value("${login.cookieDateName}")
    private String cookieDateName;
    @Value("${login.cookieSignatureName}")
    private String cookieSignatureName;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        CookiesGeneratedCsrfTokenRepository tokenRepository = new CookiesGeneratedCsrfTokenRepository();
        tokenRepository.setSalt(csrfSalt);
        tokenRepository.addCookieNames(cookieUserName, cookieDateName, cookieSignatureName);

        CsrfFilter csrfFilter = new CsrfFilter(tokenRepository);
        servletContext.addFilter("csrf", csrfFilter).addMappingForUrlPatterns(null, false, "/*");
    }

}
