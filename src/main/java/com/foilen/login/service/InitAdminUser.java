/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.foilen.login.db.dao.UserDao;
import com.foilen.login.db.domain.User;
import com.foilen.login.db.service.LoginService;
import com.foilen.login.db.service.UserService;
import com.foilen.smalltools.tools.DateTools;
import com.foilen.smalltools.tools.SecureRandomTools;

public class InitAdminUser {

    @Autowired
    private LoginService loginService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    @Value("${login.administratorEmail}")
    private String administratorEmail;

    @EventListener
    public void createAdminUser(ContextRefreshedEvent event) {

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {

                // Update the user
                boolean isNewUser = userService.findByEmail(administratorEmail) == null;
                User user = userService.createOrGetUser(administratorEmail);
                user.setAdmin(true);

                // Create a temporary password if it is a new system
                if (isNewUser) {
                    user.setUserId(SecureRandomTools.randomHexString(50));
                    loginService.createAuthWithPassword(administratorEmail, "qwerty", false, DateTools.addDate(new Date(), Calendar.HOUR, 1));
                }

                userDao.save(user);

            }
        });
    }

}
