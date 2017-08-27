/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.db.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Temporary user session. There is no user at first, but it will be added when he connects.
 */
@Entity
@Table(name = "login_token")
public class LoginToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String loginToken;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    private String redirectUrl;
    private Date expire;

    public Date getExpire() {
        return expire;
    }

    public long getId() {
        return id;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public User getUser() {
        return user;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
