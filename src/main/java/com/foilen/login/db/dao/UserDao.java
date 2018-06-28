/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foilen.login.db.domain.User;

/**
 * Manage {@link User}.
 */
public interface UserDao extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUserId(String userId);

}
