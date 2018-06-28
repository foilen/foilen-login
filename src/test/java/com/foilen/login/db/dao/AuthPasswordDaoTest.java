/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

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

import com.foilen.login.db.domain.AuthPassword;
import com.foilen.login.db.domain.User;
import com.foilen.login.test.AbstractSpringTests;

@Transactional
public class AuthPasswordDaoTest extends AbstractSpringTests {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AuthPasswordDao authPasswordDao;

    @Autowired
    private UserDao userDao;

    private AuthPassword createAuthPassword(User user, String expiration) throws ParseException {
        AuthPassword authPassword = new AuthPassword();
        authPassword.setUser(user);
        authPassword.setHashedPassword("junit");
        authPassword.setExpire(sdf.parse(expiration));
        return authPassword;
    }

    @Test
    public void testDeleteByExpireLesserThan() throws ParseException {

        authPasswordDao.deleteAll();
        userDao.deleteAll();

        userDao.flush();

        User user = new User();
        user.setEmail("junit@example.com");
        user.setUserId("a");
        user = userDao.save(user);

        // Expired
        authPasswordDao.save(createAuthPassword(user, "2000-01-01 00:00:00"));
        authPasswordDao.save(createAuthPassword(user, "2000-01-02 00:00:00"));
        authPasswordDao.save(createAuthPassword(user, "2000-01-03 00:00:00"));
        authPasswordDao.save(createAuthPassword(user, "2000-01-03 14:50:00"));

        // Not expired
        authPasswordDao.save(createAuthPassword(user, "2000-01-03 14:51:00"));
        authPasswordDao.save(createAuthPassword(user, "2000-01-03 15:00:00"));
        authPasswordDao.save(createAuthPassword(user, "2000-01-04 00:00:00"));

        // Call
        Long count = authPasswordDao.deleteByExpireLessThan(sdf.parse("2000-01-03 14:50:10"));
        Assert.assertEquals((Long) 4L, count);

        // Assert
        Assert.assertNull(authPasswordDao.findByExpire(sdf.parse("2000-01-01 00:00:00")));
        Assert.assertNull(authPasswordDao.findByExpire(sdf.parse("2000-01-02 00:00:00")));
        Assert.assertNull(authPasswordDao.findByExpire(sdf.parse("2000-01-03 00:00:00")));
        Assert.assertNull(authPasswordDao.findByExpire(sdf.parse("2000-01-03 14:50:00")));
        Assert.assertNotNull(authPasswordDao.findByExpire(sdf.parse("2000-01-03 14:51:00")));
        Assert.assertNotNull(authPasswordDao.findByExpire(sdf.parse("2000-01-03 15:00:00")));
        Assert.assertNotNull(authPasswordDao.findByExpire(sdf.parse("2000-01-04 00:00:00")));
    }

}
