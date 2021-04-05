/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.scheduledtask;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.foilen.login.db.dao.AuthPasswordDao;
import com.foilen.login.db.dao.LoginTokenDao;

@Transactional
public class LoginCleanExpiredScheduledTask {

    private final static Logger logger = LoggerFactory.getLogger(LoginCleanExpiredScheduledTask.class);

    @Autowired
    private AuthPasswordDao authPasswordDao;
    @Autowired
    private LoginTokenDao loginTokenDao;

    @Scheduled(cron = "33 */15 * * * *")
    public void cleanExpiredAuthPassword() {
        Long count = authPasswordDao.deleteByExpireLessThan(new Date());
        logger.info("Removed {} expired auth password", count);
    }

    @Scheduled(cron = "53 */15 * * * *")
    public void cleanExpiredTokens() {
        Long count = loginTokenDao.deleteByExpireLessThan(new Date());
        logger.info("Removed {} expired tokens", count);
    }
}
