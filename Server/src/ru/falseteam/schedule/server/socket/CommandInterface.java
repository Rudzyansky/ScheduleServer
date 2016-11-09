package ru.falseteam.schedule.server.socket;

import java.util.Map;

interface CommandInterface {
    String getName();

    void exec(Connection connection, Map<String, Object> map);
}
