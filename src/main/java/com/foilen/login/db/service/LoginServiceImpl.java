/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.foilen.login.db.dao.AuthPasswordDao;
import com.foilen.login.db.dao.LoginTokenDao;
import com.foilen.login.db.dao.UserDao;
import com.foilen.login.db.domain.AuthPassword;
import com.foilen.login.db.domain.LoginToken;
import com.foilen.login.db.domain.User;
import com.foilen.login.exception.UserDoesNotExistsException;
import com.foilen.login.service.PasswordHasherService;
import com.foilen.smalltools.tools.DateTools;
import com.foilen.smalltools.tools.SecureRandomTools;

@Transactional
public class LoginServiceImpl implements LoginService {

    private static final String DEFAULT_REDIRECT_URL = "/";

    @Autowired
    private AuthPasswordDao authPasswordDao;
    @Autowired
    private LoginTokenDao loginTokenDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordHasherService passwordHasherService;

    @Value("${login.loginToken.expireMinutes}")
    private int expireMinutes;

    @Override
    public String associateUserToToken(String token, User user) {

        String redirectUrl = null;

        // Get and update the login token if available
        if (token != null) {
            LoginToken loginToken = loginTokenDao.findByLoginToken(token);
            if (loginToken != null) {
                loginToken.setUser(user);
                redirectUrl = loginToken.getRedirectUrl();
                loginTokenDao.save(loginToken);
            }
        }

        // If no redirection, put the default one
        if (redirectUrl == null) {
            redirectUrl = DEFAULT_REDIRECT_URL;
        }

        return redirectUrl;
    }

    @Override
    public boolean authWithPassword(String email, String password) {

        email = email.toLowerCase();

        // Check if password in the db
        String hashedPassword = passwordHasherService.hash(email, password);
        AuthPassword authPassword = authPasswordDao.findByUserEmailAndHashedPassword(email, hashedPassword);
        if (authPassword == null) {
            return false;
        }

        // Check if expired
        if (authPassword.getExpire() != null && authPassword.getExpire().before(new Date())) {
            return false;
        }

        // Check if one time use
        if (authPassword.isSingleUse()) {
            authPasswordDao.delete(authPassword);
        }

        return true;
    }

    @Override
    public String createAuthWithPassword(String email, boolean singleUse, Date expiration) {
        String password = SecureRandomTools.randomHexString(10);
        createAuthWithPassword(email, password, singleUse, expiration);

        return password;
    }

    @Override
    public void createAuthWithPassword(String email, String password, boolean singleUse, Date expiration) {

        email = email.toLowerCase();

        // Get the user
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UserDoesNotExistsException(email);
        }

        // Create the password
        AuthPassword authPassword = new AuthPassword();
        authPassword.setUser(user);
        authPassword.setHashedPassword(passwordHasherService.hash(email, password));
        authPassword.setSingleUse(singleUse);
        authPassword.setExpire(expiration);
        authPasswordDao.save(authPassword);
    }

    @Override
    public LoginToken createToken(String redirectUrl) {
        LoginToken loginToken = new LoginToken();

        String token = SecureRandomTools.randomHexString(20);
        loginToken.setLoginToken(token);
        loginToken.setRedirectUrl(redirectUrl);

        Date expire = DateTools.addDate(new Date(), Calendar.MINUTE, expireMinutes);
        loginToken.setExpire(expire);

        return loginTokenDao.save(loginToken);
    }

    @Override
    public User retrieveLoggedUser(String token) {
        LoginToken loginToken = loginTokenDao.findByLoginToken(token);
        if (loginToken == null) {
            return null;
        }
        return loginToken.getUser();
    }

}
