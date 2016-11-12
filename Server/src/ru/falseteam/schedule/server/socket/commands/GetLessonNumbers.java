package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.LessonNumberInfo;

import java.util.Map;

public class GetLessonNumbers extends CommandAbstract {
    public GetLessonNumbers() {
        super("get_lesson_numbers");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.put("lesson_numbers", LessonNumberInfo.getLessonNumbers());
        connection.send(map);
    }
}
