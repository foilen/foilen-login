/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

@Configuration
@Profile("PROD")
public class LoginDbLiveSpringConfig {

    @Value("${login.mysqlHostName}")
    String hostName;
    @Value("${login.mysqlPort}")
    int port;
    @Value("${login.mysqlDatabaseName}")
    String databaseName;
    @Value("${login.mysqlDatabaseUserName}")
    String userName;
    @Value("${login.mysqlDatabasePassword}")
    String password;

    @Bean
    public DataSource datasource() {
        MysqlConnectionPoolDataSource datasource = new MysqlConnectionPoolDataSource();
        datasource.setUrl("jdbc:mysql://" + hostName + ":" + port + "/" + databaseName);
        datasource.setUser(userName);
        datasource.setPassword(password);
        return datasource;

    }
}
