package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.TemplateInfo;

import java.util.Map;

public class DeleteTemplate extends CommandAbstract {
    public DeleteTemplate() {
        super("delete_template");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        boolean b = TemplateInfo.deleteTemplate((Template) map.get("template"));
        map.clear();
        map.put("command", "toast_short");
        map.put("message", b ? "Шаблон удален" : "Произошла ошибка при удалении шаблона");
        connection.send(map);
    }
}
