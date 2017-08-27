/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foilen.login.db.domain.AuthPassword;

/**
 * Manage {@link AuthPassword}.
 */
public interface AuthPasswordDao extends JpaRepository<AuthPassword, Long> {

    Long deleteByExpireLessThan(Date date);

    AuthPassword findByExpire(Date parse);

    AuthPassword findByUserEmailAndHashedPassword(String email, String hashedPassword);

}
