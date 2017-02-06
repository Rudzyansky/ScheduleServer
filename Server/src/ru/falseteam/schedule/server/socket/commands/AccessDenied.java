package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ServerConnectionAbstract;
import ru.falseteam.vframe.socket.ServerProtocolAbstract;

import java.util.Map;

public class AccessDenied extends ServerProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ServerConnectionAbstract connection) {
        String command = (String) map.get("command");
        map.clear();
        map.put("command", "forbidden");
        map.put("forbidden", command);
        connection.send(new Container(getName(), map));
    }
}
