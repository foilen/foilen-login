/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login.upgrades;

import org.springframework.stereotype.Component;

import com.foilen.smalltools.upgrader.tasks.AbstractDatabaseUpgradeTask;

@Component
public class V2017082701_Initial extends AbstractDatabaseUpgradeTask {

    @Override
    public void execute() {
        if (mysqlTablesFindAll().size() >= 3) {
            return;
        }

        updateFromResource("V2017082701_Initial.sql");
    }

}
