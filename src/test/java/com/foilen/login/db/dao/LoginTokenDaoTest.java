/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.foilen.login.db.domain.LoginToken;
import com.foilen.login.test.AbstractSpringTests;

@Transactional
public class LoginTokenDaoTest extends AbstractSpringTests {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LoginTokenDao loginTokenDao;

    private int count = 0;

    private LoginToken createLoginToken(String expiration) throws ParseException {
        LoginToken loginToken = new LoginToken();
        loginToken.setLoginToken(String.valueOf(count++));
        loginToken.setExpire(sdf.parse(expiration));
        return loginToken;
    }

    @Test
    public void testDeleteByExpireLesserThan() throws ParseException {

        loginTokenDao.deleteAll();

        // Expired
        loginTokenDao.save(createLoginToken("2000-01-01 00:00:00"));
        loginTokenDao.save(createLoginToken("2000-01-02 00:00:00"));
        loginTokenDao.save(createLoginToken("2000-01-03 00:00:00"));
        loginTokenDao.save(createLoginToken("2000-01-03 14:50:00"));

        // Not expired
        loginTokenDao.save(createLoginToken("2000-01-03 14:51:00"));
        loginTokenDao.save(createLoginToken("2000-01-03 15:00:00"));
        loginTokenDao.save(createLoginToken("2000-01-04 00:00:00"));

        // Call
        Long count = loginTokenDao.deleteByExpireLessThan(sdf.parse("2000-01-03 14:50:10"));
        Assert.assertEquals((Long) 4L, count);

        // Assert
        Assert.assertNull(loginTokenDao.findByExpire(sdf.parse("2000-01-01 00:00:00")));
        Assert.assertNull(loginTokenDao.findByExpire(sdf.parse("2000-01-02 00:00:00")));
        Assert.assertNull(loginTokenDao.findByExpire(sdf.parse("2000-01-03 00:00:00")));
        Assert.assertNull(loginTokenDao.findByExpire(sdf.parse("2000-01-03 14:50:00")));
        Assert.assertNotNull(loginTokenDao.findByExpire(sdf.parse("2000-01-03 14:51:00")));
        Assert.assertNotNull(loginTokenDao.findByExpire(sdf.parse("2000-01-03 15:00:00")));
        Assert.assertNotNull(loginTokenDao.findByExpire(sdf.parse("2000-01-04 00:00:00")));

    }

}
