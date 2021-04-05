/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import org.hibernate.dialect.MySQL5Dialect;

import com.foilen.smalltools.tools.FileTools;
import com.foilen.smalltools.tools.Hibernate52Tools;

public class LoginGenSql {

    private static final String SQL_FILE = "sql/mysql.sql";

    public static void main(String[] args) {
        System.setProperty("hibernate.dialect.storage_engine", "innodb");
        FileTools.deleteFile(SQL_FILE);
        Hibernate52Tools.generateSqlSchema(MySQL5Dialect.class, SQL_FILE, true, "com.foilen.login.db.domain");
    }

}
