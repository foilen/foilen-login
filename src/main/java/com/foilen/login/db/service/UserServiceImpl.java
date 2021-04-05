/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.foilen.login.db.dao.UserDao;
import com.foilen.login.db.domain.User;
import com.foilen.smalltools.tools.SecureRandomTools;

@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User createOrGetUser(String email) {

        email = email.toLowerCase();

        // Try to find it
        User user = userDao.findByEmail(email);

        // Create one
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUserId(SecureRandomTools.randomHexString(50));

            // Save
            userDao.save(user);
        }

        return user;
    }

    @Override
    public User createOrGetUser(String email, Locale locale) {

        email = email.toLowerCase();

        // Try to find it
        User user = userDao.findByEmail(email);

        // Create one
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUserId(SecureRandomTools.randomHexString(50));
            user.setLang(locale.getLanguage());

            // Save
            userDao.save(user);
        }

        return user;
    }

    @Override
    public User findByEmail(String email) {
        email = email.toLowerCase();
        return userDao.findByEmail(email);
    }

    @Override
    public User findByUserId(String userId) {
        return userDao.findByUserId(userId);
    }

}
