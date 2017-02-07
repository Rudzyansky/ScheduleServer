package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class AccessDenied extends ProtocolAbstract {

    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        connection.send(new Container(getName(), map));
    }
}
