/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/account")
public class LoginAccountController {

    // TODO Account - Show details
    // TODO Account - Change details
    // TODO Account - Set password

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("account_index");
    };
};
