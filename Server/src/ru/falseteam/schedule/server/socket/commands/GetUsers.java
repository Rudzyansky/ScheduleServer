package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class GetUsers extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        Container c = new Container(getName(), true);
//        if (((Connection) connection).getUser().permissions == Groups.developer) ;
        c.data.put("users", UserInfo.getUsers());
        connection.send(c);
    }
}
