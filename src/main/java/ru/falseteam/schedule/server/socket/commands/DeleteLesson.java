package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.server.sql.LessonInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class DeleteLesson extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        boolean b = LessonInfo.deleteLesson((Lesson) map.get("lesson"));
        Container c = new Container("ToastShort", true);
        c.data.put("message", b ? "Предмет удален" : "Произошла ошибка при удалении предмета");
        connection.send(c);
    }
}
