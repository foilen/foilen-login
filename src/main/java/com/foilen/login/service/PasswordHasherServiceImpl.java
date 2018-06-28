/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.service;

import com.foilen.smalltools.hash.HashSha512;

public class PasswordHasherServiceImpl implements PasswordHasherService {

    @Override
    public String hash(String salt, String password) {
        return HashSha512.hashString(salt + password);
    }

}
