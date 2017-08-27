/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.controller;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foilen.login.api.to.FoilenLoginToken;
import com.foilen.login.api.to.FoilenLoginUser;
import com.foilen.login.db.domain.LoginToken;
import com.foilen.login.db.domain.User;
import com.foilen.login.db.service.LoginService;
import com.foilen.login.db.service.UserService;
import com.foilen.login.exception.ResourceNotFoundException;

@Controller
@RequestMapping("/api")
public class LoginApiController {

    private final static Logger logger = LoggerFactory.getLogger(LoginApiController.class);

    @Value("${login.applicationId}")
    private String applicationId;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    private EmailValidator emailValidator = EmailValidator.getInstance();

    @RequestMapping("createOrFindByEmail/{email}")
    @ResponseBody
    FoilenLoginUser createOrFindByEmail(@PathVariable("email") String email, @RequestParam(value = "appId", required = false) String appId) {

        // Make sure the app id is there
        if (!applicationId.equals(appId)) {
            logger.error("createOrFindByEmail() email: {} by unauthorized user", email);
            throw new ResourceNotFoundException();
        }

        logger.info("createOrFindByEmail() email: {}", email);

        // Check email
        if (!emailValidator.isValid(email)) {
            return null;
        }

        // Get the user details
        User user = userService.createOrGetUser(email);
        FoilenLoginUser result = new FoilenLoginUser();
        if (user == null) {
            return null;
        } else {
            BeanUtils.copyProperties(user, result);
        }
        return result;
    };

    @RequestMapping("createToken")
    @ResponseBody
    FoilenLoginToken createToken(@RequestParam("redirectUrl") String redirectUrl) {

        logger.info("createToken() redirectUrl: {}", redirectUrl);

        LoginToken loginToken = loginService.createToken(redirectUrl);
        FoilenLoginToken result = new FoilenLoginToken();
        result.setToken(loginToken.getLoginToken());
        result.setExpiration(loginToken.getExpire());
        return result;
    };

    @RequestMapping("findByEmail/{email}")
    @ResponseBody
    FoilenLoginUser findByEmail(@PathVariable("email") String email, @RequestParam(value = "appId", required = false) String appId) {

        // Make sure the app id is there
        if (!applicationId.equals(appId)) {
            logger.error("findByEmail() email: {} by unauthorized user", email);
            throw new ResourceNotFoundException();
        }

        logger.info("findByEmail() email: {}", email);

        // Get the user details
        User user = userService.findByEmail(email);
        FoilenLoginUser result = new FoilenLoginUser();
        if (user == null) {
            return null;
        } else {
            BeanUtils.copyProperties(user, result);
        }
        return result;
    };

    @RequestMapping("findByUserId/{userId}")
    @ResponseBody
    FoilenLoginUser findByUserId(@PathVariable("userId") String userId, @RequestParam(value = "appId", required = false) String appId) {

        // Make sure the app id is there
        if (!applicationId.equals(appId)) {
            logger.error("findByUserId() userId: {} by unauthorized user", userId);
            throw new ResourceNotFoundException();
        }

        logger.info("findByUserId() userId: {}", userId);

        // Get the user details
        User user = userService.findByUserId(userId);
        FoilenLoginUser result = new FoilenLoginUser();
        if (user == null) {
            return null;
        } else {
            BeanUtils.copyProperties(user, result);
        }
        return result;
    };

    @RequestMapping("findUserIdByToken/{token}")
    @ResponseBody
    String findUserIdByToken(@PathVariable("token") String token) {

        logger.info("findUserIdByToken() userId: {}", token);

        User user = loginService.retrieveLoggedUser(token);
        if (user != null) {
            return user.getUserId();
        }
        return null;
    };
};
