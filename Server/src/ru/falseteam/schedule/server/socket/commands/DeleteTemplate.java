package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.server.sql.TemplateInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class DeleteTemplate extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        boolean b = TemplateInfo.deleteTemplate((Template) map.get("template"));
        map.clear();
        map.put("command", "toast_short");
        map.put("message", b ? "Шаблон удален" : "Произошла ошибка при удалении шаблона");
        connection.send(new Container(getName(), map));
    }
}
