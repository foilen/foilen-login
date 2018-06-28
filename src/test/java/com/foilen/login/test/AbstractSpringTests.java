/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foilen.login.LoginSpringConfig;
import com.foilen.test.config.LoginTestSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { LoginSpringConfig.class, LoginTestSpringConfig.class })
@ActiveProfiles("JUNIT")
public abstract class AbstractSpringTests {

    public AbstractSpringTests() {
        System.setProperty("MODE", "JUNIT");
    }

}
