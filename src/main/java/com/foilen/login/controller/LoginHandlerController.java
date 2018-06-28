/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.controller;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.foilen.login.controller.form.PasswordLoginForm;
import com.foilen.login.db.dao.UserDao;
import com.foilen.login.db.domain.User;
import com.foilen.login.db.service.LoginService;
import com.foilen.login.db.service.UserService;
import com.foilen.login.service.LoginCookieService;
import com.foilen.login.service.LoginUserEmailService;
import com.foilen.smalltools.tools.DateTools;
import com.google.common.base.Strings;

@Controller
@RequestMapping("handler")
public class LoginHandlerController {

    private final static Logger logger = LoggerFactory.getLogger(LoginHandlerController.class);

    @Autowired
    private LoginCookieService loginCookieService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private LoginUserEmailService loginUserEmailService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Value("${login.loginBaseUrl}")
    String loginBaseUrl;

    @RequestMapping(value = "createOneTimePassword", method = RequestMethod.POST)
    public View createOneTimePassword(PasswordLoginForm passwordLoginForm, RedirectAttributes redirectAttrs, Locale locale) {

        if (passwordLoginForm.getEmail() != null) {
            passwordLoginForm.setEmail(passwordLoginForm.getEmail().toLowerCase());
        }

        // Check email
        if (!emailValidator.isValid(passwordLoginForm.getEmail())) {
            redirectAttrs.addFlashAttribute("error", "email.error.invalid");
            return new RedirectView("/login/" + passwordLoginForm.getToken());
        }

        logger.debug("createOneTimePassword for {}", passwordLoginForm.getEmail());
        try {

            // Create user if missing
            userService.createOrGetUser(passwordLoginForm.getEmail(), locale);

            // Create password
            String password = loginService.createAuthWithPassword(passwordLoginForm.getEmail(), true, DateTools.addDate(new Date(), Calendar.MINUTE, 15));

            Map<String, Object> model = new HashMap<>();
            model.put("password", password);

            // Create url
            StringBuilder url = new StringBuilder(loginBaseUrl).append("/handler/password?email=");
            url.append(URLEncoder.encode(passwordLoginForm.getEmail(), "UTF-8"));
            url.append("&password=").append(URLEncoder.encode(password, "UTF-8"));
            if (!Strings.isNullOrEmpty(passwordLoginForm.getToken())) {
                url.append("&token=").append(URLEncoder.encode(passwordLoginForm.getToken(), "UTF-8"));
            }
            model.put("url", url.toString());

            // Send email
            loginUserEmailService.sendEmail(passwordLoginForm.getEmail(), "login.createOneTimePassword.subject", "/com/foilen/login/emails/CreateOneTimePassword-%LANG%.ftl", model, locale);
            redirectAttrs.addFlashAttribute("success", "login.password.success.onetime.sent");
        } catch (Exception e) {
            logger.error("Problem creating a one time password", e);
            redirectAttrs.addFlashAttribute("error", "login.password.error.problem");
        }

        redirectAttrs.addFlashAttribute("form", passwordLoginForm);
        return new RedirectView("/login/" + passwordLoginForm.getToken());
    }

    @RequestMapping(value = "password", method = RequestMethod.GET)
    public View passwordGet(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam(value = "token", required = false) String token,
            RedirectAttributes redirectAttrs, HttpServletResponse response) {
        PasswordLoginForm passwordLoginForm = new PasswordLoginForm();
        passwordLoginForm.setEmail(email);
        passwordLoginForm.setPassword(password);
        passwordLoginForm.setToken(token);
        return passwordPost(passwordLoginForm, redirectAttrs, response);
    }

    @RequestMapping(value = "password", method = RequestMethod.POST)
    public View passwordPost(PasswordLoginForm passwordLoginForm, RedirectAttributes redirectAttrs, HttpServletResponse response) {

        // Check if right email/password
        if (loginService.authWithPassword(passwordLoginForm.getEmail(), passwordLoginForm.getPassword())) {
            User user = userDao.findByEmail(passwordLoginForm.getEmail().toLowerCase());
            String redirection = loginService.associateUserToToken(passwordLoginForm.getToken(), user);
            loginCookieService.setLoggedInUser(user, response);
            return new RedirectView(redirection);
        } else {
            redirectAttrs.addFlashAttribute("error", "login.password.error.invalidCredential");
            redirectAttrs.addFlashAttribute("form", passwordLoginForm);
            if (Strings.isNullOrEmpty(passwordLoginForm.getToken())) {
                return new RedirectView("/login/");
            } else {
                return new RedirectView("/login/" + passwordLoginForm.getToken());
            }
        }

    };
};
