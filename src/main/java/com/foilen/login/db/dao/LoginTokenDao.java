/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foilen.login.db.domain.LoginToken;

/**
 * Manage {@link LoginToken}.
 */
public interface LoginTokenDao extends JpaRepository<LoginToken, Long> {

    Long deleteByExpireLessThan(Date date);

    LoginToken findByExpire(Date expire);

    LoginToken findByLoginToken(String loginToken);

}
