/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.service;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.foilen.login.db.dao.UserDao;
import com.foilen.login.db.domain.User;
import com.foilen.smalltools.hash.HashSha512;
import com.foilen.smalltools.tools.DateTools;
import com.google.common.base.Strings;

public class LoginCookieServiceImpl implements LoginCookieService {

    @Autowired
    private UserDao userDao;

    @Value("${login.cookieUserName}")
    private String cookieUserName;

    @Value("${login.cookieDateName}")
    private String cookieDateName;

    @Value("${login.cookieSignatureName}")
    private String cookieSignatureName;

    @Value("${login.cookieSignatureSalt}")
    private String cookieSignatureSalt;

    @Value("${login.cookie.expirationDays}")
    private int cookieExpirationDays;

    private void addCookies(User user, HttpServletResponse response) {
        if (user == null) {
            response.addCookie(createCookie(cookieUserName, null));
            response.addCookie(createCookie(cookieSignatureName, null));
            response.addCookie(createCookie(cookieDateName, null));
        } else {
            Date date = new Date();
            response.addCookie(createCookie(cookieUserName, user.getUserId()));
            response.addCookie(createCookie(cookieSignatureName, HashSha512.hashString(user.getUserId() + date.getTime() + cookieSignatureSalt)));
            response.addCookie(createCookie(cookieDateName, String.valueOf(date.getTime())));
        }
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieExpirationDays * 24 * 60 * 60);
        cookie.setPath("/");

        if (value == null) {
            cookie.setMaxAge(0);
        }

        return cookie;
    }

    private User findUser(Cookie[] cookies) {

        if (cookies == null) {
            return null;
        }

        String cUserId = null;
        String cSignature = null;
        Date cDate = null;
        for (Cookie cookie : cookies) {
            if (cookieUserName.equals(cookie.getName())) {
                cUserId = cookie.getValue();
            }
            if (cookieSignatureName.equals(cookie.getName())) {
                cSignature = cookie.getValue();
            }
            if (cookieDateName.equals(cookie.getName())) {
                try {
                    cDate = new Date(Long.valueOf(cookie.getValue()));
                } catch (Exception e) {
                }
            }
        }

        // Check if there is one cookie and it is the right signature
        User user = null;
        if (!Strings.isNullOrEmpty(cUserId) && !Strings.isNullOrEmpty(cSignature) && cDate != null) {
            // Check not expired
            if (!DateTools.isExpired(cDate, Calendar.DAY_OF_MONTH, cookieExpirationDays)) {
                // Check signature
                String correctSignature = HashSha512.hashString(cUserId + cDate.getTime() + cookieSignatureSalt);
                if (correctSignature.equals(cSignature)) {
                    user = userDao.findByUserId(cUserId);
                }
            }
        }

        return user;
    }

    @Override
    public User getLoggedInUser(HttpServletRequest request) {
        return findUser(request.getCookies());
    }

    @Override
    public void removeLoggedInUser(HttpServletResponse response) {
        addCookies(null, response);
    }

    @Override
    public void setLoggedInUser(User user, HttpServletResponse response) {
        addCookies(user, response);
    }

}
