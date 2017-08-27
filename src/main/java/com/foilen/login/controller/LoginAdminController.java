/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class LoginAdminController {

    // TODO Admin - Create user
    // TODO Admin - List users
    // TODO Admin - Manage user permissions

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("admin_index");
    };

};
