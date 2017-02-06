package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.sql.LessonNumberInfo;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ServerConnectionAbstract;
import ru.falseteam.vframe.socket.ServerProtocolAbstract;

import java.util.Map;

public class GetLessonNumbers extends ServerProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ServerConnectionAbstract connection) {
        map.put("lesson_numbers", LessonNumberInfo.getLessonNumbers());
        connection.send(new Container(getName(), map));
    }
}
