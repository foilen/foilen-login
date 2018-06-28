/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foilen.login.db.domain.User;

/**
 * Manages the login cookie and signature.
 */
public interface LoginCookieService {

    User getLoggedInUser(HttpServletRequest request);

    void removeLoggedInUser(HttpServletResponse response);

    void setLoggedInUser(User user, HttpServletResponse response);
}
