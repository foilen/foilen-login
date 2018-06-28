/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017-2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

import com.foilen.login.controller.LoginAccountController;
import com.foilen.login.controller.LoginAdminController;
import com.foilen.login.controller.LoginApiController;
import com.foilen.login.controller.LoginHandlerController;
import com.foilen.login.controller.LoginHomeController;
import com.foilen.login.db.service.LoginServiceImpl;
import com.foilen.login.db.service.UserServiceImpl;
import com.foilen.login.scheduledtask.LoginCleanExpiredScheduledTask;
import com.foilen.login.service.InitAdminUser;
import com.foilen.login.service.LoginCookieServiceImpl;
import com.foilen.login.service.LoginUserEmailServiceImpl;
import com.foilen.login.service.PasswordHasherServiceImpl;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.smalltools.tools.LogbackTools;
import com.foilen.smalltools.tools.SecureRandomTools;
import com.foilen.smalltools.tools.SpringTools;
import com.google.common.base.Strings;

/**
 * The web application to login and manage users.
 */
public class LoginApp {

    static private final Logger logger = LoggerFactory.getLogger(LoginApp.class);

    public static void main(String[] args) {

        try {
            // Get the parameters
            LoginOptions loginOptions = new LoginOptions();
            CmdLineParser cmdLineParser = new CmdLineParser(loginOptions);
            try {
                cmdLineParser.parseArgument(args);
            } catch (CmdLineException e) {
                e.printStackTrace();
                showUsage();
                return;
            }

            List<String> springBootArgs = new ArrayList<String>();
            if (loginOptions.debug) {
                springBootArgs.add("--debug");
            }

            // Set the environment
            String mode = loginOptions.mode;
            ConfigurableEnvironment environment = new StandardServletEnvironment();
            environment.addActiveProfile(mode);
            System.setProperty("MODE", mode);

            // Get the configuration from options or environment
            String configFile = loginOptions.configFile;
            if (Strings.isNullOrEmpty(configFile)) {
                configFile = environment.getProperty("CONFIG_FILE");
            }
            LoginConfig loginConfig;
            if (Strings.isNullOrEmpty(configFile)) {
                loginConfig = new LoginConfig();
            } else {
                loginConfig = JsonTools.readFromFile(configFile, LoginConfig.class);
            }

            // Local -> Add some mock values
            if ("LOCAL".equals(mode)) {
                logger.info("Setting some random values for LOCAL mode");
                loginConfig.setCookieSignatureSalt(SecureRandomTools.randomBase64String(10));
                loginConfig.setCsrfSalt(SecureRandomTools.randomBase64String(10));
                loginConfig.setApplicationId(SecureRandomTools.randomBase64String(10));

                loginConfig.setFromEmail("login@example.com");
                loginConfig.setAdministratorEmail("admin@example.com");

                loginConfig.setLoginBaseUrl("http://login.example.com");

                loginConfig.setMysqlDatabaseUserName("notNeeded");
                loginConfig.setMysqlDatabasePassword("notNeeded");
            }

            // Override some database configuration if provided via environment
            String overrideMysqlHostName = System.getenv("MYSQL_PORT_3306_TCP_ADDR");
            if (!Strings.isNullOrEmpty(overrideMysqlHostName)) {
                loginConfig.setMysqlHostName(overrideMysqlHostName);
            }
            String overrideMysqlPort = System.getenv("MYSQL_PORT_3306_TCP_PORT");
            if (!Strings.isNullOrEmpty(overrideMysqlPort)) {
                loginConfig.setMysqlPort(Integer.valueOf(overrideMysqlPort));
            }

            // Check needed config and add it to the known properties
            BeanWrapper loginConfigBeanWrapper = new BeanWrapperImpl(loginConfig);
            for (PropertyDescriptor propertyDescriptor : loginConfigBeanWrapper.getPropertyDescriptors()) {
                String propertyName = propertyDescriptor.getName();
                Object propertyValue = loginConfigBeanWrapper.getPropertyValue(propertyName);
                if (propertyValue == null || propertyValue.toString().isEmpty()) {
                    System.err.println(propertyName + " in the config cannot be null or empty");
                    System.exit(1);
                } else {
                    System.setProperty("login." + propertyName, propertyValue.toString());
                }
            }

            // Run the upgrader
            List<Class<?>> sources = new ArrayList<>();
            if ("LOCAL".equals(mode)) {
                logger.info("Skipping UPGRADE MODE");
            } else {
                logger.info("Begin UPGRADE MODE");
                sources.add(LoginUpgradesSpringConfig.class);
                sources.add(LoginDbLiveSpringConfig.class);
                SpringApplication springApplication = new SpringApplication(sources.toArray());
                springApplication.setEnvironment(environment);
                springApplication.run(springBootArgs.toArray(new String[springBootArgs.size()]));
                logger.info("End UPGRADE MODE");
            }

            // Run the main app
            logger.info("Will start the main app");
            sources.clear();
            sources.add(LoginSpringConfig.class);
            sources.add(MvcSpringConfig.class);
            sources.add(MailSpringConfig.class);

            sources.add(LoginAccountController.class);
            sources.add(LoginAdminController.class);
            sources.add(LoginApiController.class);
            sources.add(LoginHandlerController.class);
            sources.add(LoginHomeController.class);

            sources.add(LoginServiceImpl.class);
            sources.add(LoginCookieServiceImpl.class);
            sources.add(LoginUserEmailServiceImpl.class);
            sources.add(PasswordHasherServiceImpl.class);
            sources.add(SpringTools.class);
            sources.add(UserServiceImpl.class);

            sources.add(InitAdminUser.class);

            sources.add(LoginCleanExpiredScheduledTask.class);

            SpringApplication springApplication = new SpringApplication(sources.toArray());
            springApplication.setEnvironment(environment);
            springApplication.run(springBootArgs.toArray(new String[springBootArgs.size()]));

            // Check if debug
            if (loginOptions.debug) {
                LogbackTools.changeConfig("/logback-debug.xml");
            }
        } catch (Exception e) {
            logger.error("Application failed", e);
            System.exit(1);
        }

    }

    private static void showUsage() {
        System.out.println("Usage:");
        CmdLineParser cmdLineParser = new CmdLineParser(new LoginOptions());
        cmdLineParser.printUsage(System.out);
    }

}
