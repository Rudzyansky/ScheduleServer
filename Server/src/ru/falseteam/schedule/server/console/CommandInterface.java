package ru.falseteam.schedule.server.console;

/**
 * @author Vladislav Sumin
 */
interface CommandInterface {
    void exec(String params);

    String getName();
}
