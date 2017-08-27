/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.exception;

public class UserDoesNotExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserDoesNotExistsException(String email) {
        super("The user " + email + " does not exists");
    }

}
