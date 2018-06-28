/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.foilen.login.db.domain.User;
import com.foilen.login.db.service.LoginService;
import com.foilen.login.service.LoginCookieService;

@Controller
@RequestMapping("/")
public class LoginHomeController {

    @Autowired
    public LoginCookieService loginCookieService;

    @Autowired
    public LoginService loginService;

    @ModelAttribute("user")
    public User getUser(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    };

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("home");
    }

    @RequestMapping("/login/{token}")
    public ModelAndView login(@PathVariable("token") String token, @ModelAttribute("user") User user) {

        // Already logged in user
        if (user != null) {
            String redirectUrl = loginService.associateUserToToken(token, user);
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("token", token);

        return modelAndView;
    };

    @RequestMapping({ "/login", "/login/" })
    public ModelAndView login(@ModelAttribute("user") User user) {
        return login("", user);
    };

    @RequestMapping("/logout")
    public View logout(HttpServletResponse response) {
        loginCookieService.removeLoggedInUser(response);
        return new RedirectView("/");
    };
};
