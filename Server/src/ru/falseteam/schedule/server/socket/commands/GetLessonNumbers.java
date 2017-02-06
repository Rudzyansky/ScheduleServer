package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.sql.LessonNumberInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class GetLessonNumbers extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        map.put("lesson_numbers", LessonNumberInfo.getLessonNumbers());
        connection.send(new Container(getName(), map));
    }
}
