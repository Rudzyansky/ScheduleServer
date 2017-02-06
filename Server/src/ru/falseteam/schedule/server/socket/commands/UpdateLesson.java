package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.server.sql.LessonInfo;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ServerConnectionAbstract;
import ru.falseteam.vframe.socket.ServerProtocolAbstract;

import java.util.Map;

public class UpdateLesson extends ServerProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ServerConnectionAbstract connection) {
        Lesson lesson = (Lesson) map.get("lesson");
        map.clear();
        map.put("command", "toast_short");
        boolean b = lesson.exists ? LessonInfo.updateLesson(lesson) : LessonInfo.addLesson(lesson);
        map.put("message", b ? "Предмет изменен" : "Произошла ошибка при изменении предмета");
        connection.send(new Container(getName(), map));
    }
}
