package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.UserInfo;

import java.util.Map;

public class GetUsers extends CommandAbstract {
    public GetUsers() {
        super("get_users");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.put("users", UserInfo.getUsers());
        connection.send(map);
    }
}
