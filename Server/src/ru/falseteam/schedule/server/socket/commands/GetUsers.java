package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ServerConnectionAbstract;
import ru.falseteam.vframe.socket.ServerProtocolAbstract;

import java.util.Map;

public class GetUsers extends ServerProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ServerConnectionAbstract connection) {
        map.put("users", UserInfo.getUsers());
        connection.send(new Container(getName(), map));
    }
}
