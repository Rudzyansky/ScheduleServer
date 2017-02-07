package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.sql.LessonInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class GetLessons extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        Container c = new Container(getName(), true);
        c.data.put("lessons", LessonInfo.getLessons());
        connection.send(c);
    }
}
