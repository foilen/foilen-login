/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.foilen.login.db.domain.User;
import com.foilen.login.service.LoginCookieService;

public class AddUserDetailsModelExtension implements AsyncHandlerInterceptor {

    @Autowired
    private LoginCookieService loginCookieService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            User user = loginCookieService.getLoggedInUser(request);
            modelAndView.addObject("user", user);
            if (user == null) {
                modelAndView.addObject("isAdmin", false);
            } else {
                modelAndView.addObject("isAdmin", user.isAdmin());
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = loginCookieService.getLoggedInUser(request);
        request.setAttribute("user", user);
        return true;
    }

}
