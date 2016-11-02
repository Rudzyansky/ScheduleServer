package ru.falseteam.schedule.server.console;

import org.apache.commons.cli.CommandLine;

/**
 * @author Vladislav Sumin
 */
interface CommandInterface {
    void exec(String params);
    void exec(CommandLine commandLine);

    String getName();
}
