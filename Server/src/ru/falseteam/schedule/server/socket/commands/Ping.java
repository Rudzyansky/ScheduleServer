package ru.falseteam.schedule.server.socket.commands;


import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;

import java.util.HashMap;
import java.util.Map;

public class Ping extends CommandAbstract {
    public Ping() {
        super("ping");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        connection.setLastPing(System.currentTimeMillis());
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("command", "ping");
        return request;
    }
}
