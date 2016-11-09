package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.LessonInfo;

import java.util.Map;

public class DeleteLesson extends CommandAbstract {
    public DeleteLesson() {
        super("delete_lesson");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        boolean b = LessonInfo.deleteLesson((Lesson) map.get("lesson"));
        map.clear();
        map.put("command", "toast_short");
        map.put("message", b ? "Предмет удален" : "Произошла ошибка при удалении предмета");
        connection.send(map);
    }
}
