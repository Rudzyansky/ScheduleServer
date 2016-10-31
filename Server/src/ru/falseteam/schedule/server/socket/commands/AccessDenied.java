package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;

import java.util.Map;

public class AccessDenied extends CommandAbstract {
    public AccessDenied() {
        super("forbidden");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.clear();
        map.put("command", "forbidden");
        connection.send(map);
    }
}
