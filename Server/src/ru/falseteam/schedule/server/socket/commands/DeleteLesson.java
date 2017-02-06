package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.server.sql.LessonInfo;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ServerConnectionAbstract;
import ru.falseteam.vframe.socket.ServerProtocolAbstract;

import java.util.Map;

public class DeleteLesson extends ServerProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ServerConnectionAbstract connection) {
        boolean b = LessonInfo.deleteLesson((Lesson) map.get("lesson"));
        map.clear();
        map.put("command", "toast_short");
        map.put("message", b ? "Предмет удален" : "Произошла ошибка при удалении предмета");
        connection.send(new Container(getName(), map));
    }
}
