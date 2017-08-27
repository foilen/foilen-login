/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foilen.login.db.service.LoginService;
import com.foilen.login.db.service.LoginServiceImpl;
import com.foilen.login.db.service.UserService;
import com.foilen.login.db.service.UserServiceImpl;
import com.foilen.login.service.LoginCookieService;
import com.foilen.login.service.LoginCookieServiceImpl;
import com.foilen.login.service.PasswordHasherService;
import com.foilen.login.service.PasswordHasherServiceImpl;

@Configuration
public class LoginTestSpringConfig {

    @Bean
    public LoginCookieService loginCookieService() {
        return new LoginCookieServiceImpl();
    }

    @Bean
    public LoginService loginService() {
        return new LoginServiceImpl();
    }

    @Bean
    public PasswordHasherService passwordHasherService() {
        return new PasswordHasherServiceImpl();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

}
