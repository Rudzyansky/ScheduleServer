package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.TemplateInfo;

import java.util.Map;

public class UpdateTemplate extends CommandAbstract {
    public UpdateTemplate() {
        super("update_template");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        Template template = (Template) map.get("template");
        map.clear();
        map.put("command", "toast_short");
        boolean b = template.exists ? TemplateInfo.updateTemplate(template) : TemplateInfo.addTemplate(template);
        map.put("message", b ? "Шаблон изменен" : "Произошла ошибка при изменении шаблона");
        connection.send(map);
    }
}
