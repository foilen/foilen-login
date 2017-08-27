/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import org.hibernate.dialect.MySQL5InnoDBDialect;

import com.foilen.smalltools.tools.Hibernate5Tools;

public class LoginGenSql {

    public static void main(String[] args) {
        Hibernate5Tools.generateSqlSchema(MySQL5InnoDBDialect.class, "sql/mysql.sql", true, "com.foilen.login.db.domain");
    }

}
