/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.service;

/**
 * Hash the password with a common algorithm.
 */
public interface PasswordHasherService {

    /**
     * Hash the password.
     *
     * @param salt
     *            the salt (could be the email)
     * @param password
     *            the plain text password
     * @return the hashed password
     */
    String hash(String salt, String password);

}
