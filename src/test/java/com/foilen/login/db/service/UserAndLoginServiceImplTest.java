/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foilen.login.db.dao.AuthPasswordDao;
import com.foilen.login.db.dao.LoginTokenDao;
import com.foilen.login.db.dao.UserDao;
import com.foilen.login.db.domain.LoginToken;
import com.foilen.login.db.domain.User;
import com.foilen.login.exception.UserDoesNotExistsException;
import com.foilen.login.test.AbstractSpringTests;
import com.google.common.base.Strings;

public class UserAndLoginServiceImplTest extends AbstractSpringTests {

    private static final String GOOD_PASSWORD = "qwerty";

    private static final String JUNIT_EMAIL = "junit@example.com";
    private static final String JUNIT_EMAIL_MIXED_1 = "jUnit@ExaMple.com";
    private static final String JUNIT_EMAIL_MIXED_2 = "JUniT@eXampLe.com";

    @Autowired
    private AuthPasswordDao authPasswordDao;

    @Autowired
    private LoginTokenDao loginTokenDao;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Before
    public void before() {
        // Clear data
        loginTokenDao.deleteAll();
        authPasswordDao.deleteAll();
        userDao.deleteAll();
        Assert.assertEquals(0, userDao.count());
        Assert.assertEquals(0, authPasswordDao.count());
        Assert.assertEquals(0, loginTokenDao.count());
    }

    @Test
    public void test() {

        // Create a user
        User junitUser = userService.createOrGetUser(JUNIT_EMAIL);
        Assert.assertNotNull(junitUser);
        Assert.assertEquals(1, userDao.count());
        junitUser.setFirstName("unit");
        userDao.save(junitUser);

        // Create another user
        User user = userService.createOrGetUser("junit2@example.com");
        Assert.assertNotNull(user);
        Assert.assertEquals(2, userDao.count());

        // Retrieve the first user
        user = userService.createOrGetUser(JUNIT_EMAIL);
        Assert.assertNotNull(user);
        Assert.assertEquals(2, userDao.count());
        Assert.assertEquals("unit", user.getFirstName());

        // Try login in with password (fail)
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, ""));
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, GOOD_PASSWORD));

        // Create a login
        loginService.createAuthWithPassword(JUNIT_EMAIL, GOOD_PASSWORD, true, null);
        Assert.assertEquals(1, authPasswordDao.count());

        // Create a login for invalid user (fail)
        boolean hadException = false;
        try {
            loginService.createAuthWithPassword("junit3@example.com", GOOD_PASSWORD, true, null);
        } catch (UserDoesNotExistsException e) {
            hadException = true;
        }
        Assert.assertTrue("A UserDoesNotExistsException should have been thrown", hadException);

        // Create a temporary login
        String tmpPassword = loginService.createAuthWithPassword(JUNIT_EMAIL, false, null);
        Assert.assertEquals(2, authPasswordDao.count());
        Assert.assertFalse(Strings.isNullOrEmpty(tmpPassword));

        // Try login with the single use password
        Assert.assertEquals(2, authPasswordDao.count());
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, ""));
        Assert.assertEquals(2, authPasswordDao.count());
        Assert.assertTrue(loginService.authWithPassword(JUNIT_EMAIL, GOOD_PASSWORD));
        Assert.assertEquals(1, authPasswordDao.count()); // Count decreased
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, GOOD_PASSWORD));

        // Try login with tmp password
        Assert.assertEquals(1, authPasswordDao.count());
        Assert.assertTrue(loginService.authWithPassword(JUNIT_EMAIL, tmpPassword));
        Assert.assertEquals(1, authPasswordDao.count());

        // Try login with invalid user
        Assert.assertFalse(loginService.authWithPassword("junit3@example.com", GOOD_PASSWORD));
        Assert.assertFalse(loginService.authWithPassword("junit3@example.com", tmpPassword));

        // Try login with the other user
        Assert.assertFalse(loginService.authWithPassword("junit2@example.com", GOOD_PASSWORD));
        Assert.assertFalse(loginService.authWithPassword("junit2@example.com", tmpPassword));

        // Test expiration , not expired
        loginService.createAuthWithPassword(JUNIT_EMAIL, "expired", false, new Date(new Date().getTime() - 60000));
        Assert.assertEquals(2, authPasswordDao.count());

        // Test expiration , expired
        loginService.createAuthWithPassword(JUNIT_EMAIL, "notexpired", false, new Date(new Date().getTime() + 60000));
        Assert.assertEquals(3, authPasswordDao.count());

        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, "expired"));
        Assert.assertTrue(loginService.authWithPassword(JUNIT_EMAIL, "notexpired"));
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, "expired"));
        Assert.assertTrue(loginService.authWithPassword(JUNIT_EMAIL, "notexpired"));
    }

    @Test
    public void testMixedCase() {

        // Create a user
        User junitUser = userService.createOrGetUser(JUNIT_EMAIL_MIXED_1);
        Assert.assertNotNull(junitUser);
        Assert.assertEquals(1, userDao.count());
        junitUser.setFirstName("unit");
        userDao.save(junitUser);

        // Retrieve the first user
        User user = userService.createOrGetUser(JUNIT_EMAIL_MIXED_2);
        Assert.assertNotNull(user);
        Assert.assertEquals("unit", user.getFirstName());
        Assert.assertEquals(JUNIT_EMAIL, user.getEmail());

        // Try login in with password (fail)
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, ""));
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, GOOD_PASSWORD));

        // Create a login
        loginService.createAuthWithPassword(JUNIT_EMAIL_MIXED_1, GOOD_PASSWORD, true, null);
        Assert.assertEquals(1, authPasswordDao.count());

        // Create a temporary login
        String tmpPassword = loginService.createAuthWithPassword(JUNIT_EMAIL_MIXED_2, false, null);
        Assert.assertEquals(2, authPasswordDao.count());
        Assert.assertFalse(Strings.isNullOrEmpty(tmpPassword));

        // Try login with the single use password
        Assert.assertEquals(2, authPasswordDao.count());
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL, ""));
        Assert.assertEquals(2, authPasswordDao.count());
        Assert.assertTrue(loginService.authWithPassword(JUNIT_EMAIL_MIXED_2, GOOD_PASSWORD));
        Assert.assertEquals(1, authPasswordDao.count()); // Count decreased
        Assert.assertFalse(loginService.authWithPassword(JUNIT_EMAIL_MIXED_2, GOOD_PASSWORD));

        // Try login with tmp password
        Assert.assertEquals(1, authPasswordDao.count());
        Assert.assertTrue(loginService.authWithPassword(JUNIT_EMAIL_MIXED_1, tmpPassword));
        Assert.assertEquals(1, authPasswordDao.count());

    }

    @Test
    public void testRetrieveLoggedUser() {

        // Create a user
        User junitUser = userService.createOrGetUser(JUNIT_EMAIL);
        Assert.assertNotNull(junitUser);
        Assert.assertEquals(1, userDao.count());
        junitUser.setFirstName("unit");
        userDao.save(junitUser);

        // Test no token
        Assert.assertNull(loginService.retrieveLoggedUser("tok"));

        // Create a token
        LoginToken loginToken = loginService.createToken(null);
        Assert.assertNotNull(loginToken);
        String token = loginToken.getLoginToken();

        // Test not logged
        Assert.assertNull(loginService.retrieveLoggedUser(token));

        // Set a user on the token
        loginToken.setUser(junitUser);
        loginToken = loginTokenDao.save(loginToken);

        // Test logged in
        Assert.assertNotNull(loginService.retrieveLoggedUser(token));
    }
}
