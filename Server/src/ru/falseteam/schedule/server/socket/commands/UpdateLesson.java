package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.LessonInfo;

import java.util.Map;

public class UpdateLesson extends CommandAbstract {
    public UpdateLesson() {
        super("update_lesson");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        Lesson lesson = (Lesson) map.get("lesson");
        map.clear();
        map.put("command", "toast_short");
        boolean b = lesson.exists ? LessonInfo.updateLesson(lesson) : LessonInfo.addLesson(lesson);
        map.put("message", b ? "Предмет изменен" : "Произошла ошибка при изменении предмета");
        connection.send(map);
    }
}
