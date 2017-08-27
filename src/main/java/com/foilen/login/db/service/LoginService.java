/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.service;

import java.util.Date;

import com.foilen.login.db.domain.LoginToken;
import com.foilen.login.db.domain.User;

/**
 * Manages the login sessions.
 */
public interface LoginService {

    /**
     * Put the user on the token and retrieve the redirect.
     *
     * @param token
     *            the login token
     * @param user
     *            the user
     * @return the redirection url
     */
    String associateUserToToken(String token, User user);

    /**
     * Check if can use the password to log into that account.
     *
     * @param email
     *            the user email
     * @param password
     *            the password
     * @return true if can login
     */
    boolean authWithPassword(String email, String password);

    /**
     * Create a random password and store it.
     *
     * @param email
     *            the user email
     * @param singleUse
     *            true if the user can login only once with it
     * @param expiration
     *            the expiration date for this password or null
     * @return the random password
     */
    String createAuthWithPassword(String email, boolean singleUse, Date expiration);

    /**
     * Create a password and store it.
     *
     * @param email
     *            the user email
     * @param password
     *            the password to use
     * @param singleUse
     *            true if the user can login only once with it
     * @param expiration
     *            the expiration date for this password or null
     */
    void createAuthWithPassword(String email, String password, boolean singleUse, Date expiration);

    /**
     * Create a temporary token.
     *
     * @param redirectUrl
     *            the url to redirect to when successfully logged in
     * @return the created token
     */
    LoginToken createToken(String redirectUrl);

    /**
     * Get the user for the token.
     *
     * @param token
     *            the login token
     * @return the user if the token exists and is logged in
     */
    User retrieveLoggedUser(String token);

}
