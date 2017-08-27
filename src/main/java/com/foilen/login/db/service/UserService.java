/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.service;

import java.util.Locale;

import com.foilen.login.db.domain.User;

/**
 * Manages the users in the DB.
 */
public interface UserService {

    /**
     * Create a user if it does exists or retrieve the existing one.
     *
     * @param email
     *            the user email
     * @return the user
     */
    User createOrGetUser(String email);

    /**
     * Create a user with the specified locale if it does exists or retrieve the existing one.
     *
     * @param email
     *            the user email
     * @param locale
     *            the current locale
     * @return the user
     */
    User createOrGetUser(String email, Locale locale);

    User findByEmail(String email);

    /**
     * Get the user with the specified user id.
     *
     * @param userId
     *            the user id
     * @return the user of null if does not exists
     */
    User findByUserId(String userId);

}
