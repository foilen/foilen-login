/*
    Foilen Login
    https://github.com/foilen/foilen-login
    Copyright (c) 2017 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.login;

import org.kohsuke.args4j.Option;

/**
 * The arguments to pass to the login web application.
 */
public class LoginOptions {

    @Option(name = "--debug", usage = "To log everything (default: false)")
    public boolean debug;

    @Option(name = "--configFile", usage = "The config file path (default: none since using the CONFIG_FILE environment variable)")
    public String configFile;

    @Option(name = "--mode", usage = "The mode: LOCAL, TEST or PROD (default: PROD)")
    public String mode = "PROD";

}
