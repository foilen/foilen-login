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
 * Authenticate via passwords. The login is the email address.
 */
@Entity
@Table(name = "auth_password")
public class AuthPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String hashedPassword;

    private boolean singleUse = false;
    private Date expire;

    public Date getExpire() {
        return expire;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
