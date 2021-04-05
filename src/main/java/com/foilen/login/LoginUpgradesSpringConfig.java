/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2021 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foilen.smalltools.upgrader.UpgraderTools;
import com.foilen.smalltools.upgrader.tasks.UpgradeTask;
import com.foilen.smalltools.upgrader.trackers.DatabaseUpgraderTracker;

@Configuration
@ComponentScan({ "com.foilen.login.upgrades" })
@EnableAutoConfiguration(exclude = { //
        HibernateJpaAutoConfiguration.class, //
        JpaRepositoriesAutoConfiguration.class, //
        WebMvcAutoConfiguration.class, //
})
@PropertySource({ "classpath:/com/foilen/login/application.properties", "classpath:/com/foilen/login/application-${MODE}.properties" })
public class LoginUpgradesSpringConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public UpgraderTools upgraderTools(List<UpgradeTask> tasks) {
        UpgraderTools upgraderTools = new UpgraderTools(tasks);
        upgraderTools.addUpgraderTracker("db", new DatabaseUpgraderTracker(jdbcTemplate));
        return upgraderTools;
    }

}
