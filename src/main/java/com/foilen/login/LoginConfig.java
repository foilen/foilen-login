/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginConfig {

    private String mysqlHostName = "127.0.0.1";
    private int mysqlPort = 3306;
    private String mysqlDatabaseName = "foilen_login";
    private String mysqlDatabaseUserName;
    private String mysqlDatabasePassword;

    private String cookieUserName = "fl_user_id";
    private String cookieDateName = "fl_date";
    private String cookieSignatureName = "fl_signature";
    private String cookieSignatureSalt;

    private String csrfSalt;

    private String applicationId;

    private String fromEmail;
    private String administratorEmail;
    private String mailHost = "127.0.0.1";
    private int mailPort = 25;
    @Nullable
    private String mailUsername; // Optional
    @Nullable
    private String mailPassword; // Optional

    private String loginBaseUrl;

    public String getAdministratorEmail() {
        return administratorEmail;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getCookieDateName() {
        return cookieDateName;
    }

    public String getCookieSignatureName() {
        return cookieSignatureName;
    }

    public String getCookieSignatureSalt() {
        return cookieSignatureSalt;
    }

    public String getCookieUserName() {
        return cookieUserName;
    }

    public String getCsrfSalt() {
        return csrfSalt;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getLoginBaseUrl() {
        return loginBaseUrl;
    }

    public String getMailHost() {
        return mailHost;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public int getMailPort() {
        return mailPort;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getMysqlDatabaseName() {
        return mysqlDatabaseName;
    }

    public String getMysqlDatabasePassword() {
        return mysqlDatabasePassword;
    }

    public String getMysqlDatabaseUserName() {
        return mysqlDatabaseUserName;
    }

    public String getMysqlHostName() {
        return mysqlHostName;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public void setAdministratorEmail(String administratorEmail) {
        this.administratorEmail = administratorEmail;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setCookieDateName(String cookieDateName) {
        this.cookieDateName = cookieDateName;
    }

    public void setCookieSignatureName(String cookieSignatureName) {
        this.cookieSignatureName = cookieSignatureName;
    }

    public void setCookieSignatureSalt(String cookieSignatureSalt) {
        this.cookieSignatureSalt = cookieSignatureSalt;
    }

    public void setCookieUserName(String cookieUserName) {
        this.cookieUserName = cookieUserName;
    }

    public void setCsrfSalt(String csrfSalt) {
        this.csrfSalt = csrfSalt;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setLoginBaseUrl(String loginBaseUrl) {
        this.loginBaseUrl = loginBaseUrl;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public void setMailPort(int mailPort) {
        this.mailPort = mailPort;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public void setMysqlDatabaseName(String mysqlDatabaseName) {
        this.mysqlDatabaseName = mysqlDatabaseName;
    }

    public void setMysqlDatabasePassword(String mysqlDatabasePassword) {
        this.mysqlDatabasePassword = mysqlDatabasePassword;
    }

    public void setMysqlDatabaseUserName(String mysqlDatabaseUserName) {
        this.mysqlDatabaseUserName = mysqlDatabaseUserName;
    }

    public void setMysqlHostName(String mysqlHostName) {
        this.mysqlHostName = mysqlHostName;
    }

    public void setMysqlPort(int mysqlPort) {
        this.mysqlPort = mysqlPort;
    }

}
