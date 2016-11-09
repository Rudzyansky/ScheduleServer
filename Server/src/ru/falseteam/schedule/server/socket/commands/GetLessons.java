package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.LessonInfo;

import java.util.Map;

public class GetLessons extends CommandAbstract {
    public GetLessons() {
        super("get_lessons");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.put("lessons", LessonInfo.getLessons());
        connection.send(map);
    }
}
