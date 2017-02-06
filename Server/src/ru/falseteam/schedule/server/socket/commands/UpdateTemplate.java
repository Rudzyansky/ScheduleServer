package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.server.sql.TemplateInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class UpdateTemplate extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        Template template = (Template) map.get("template");
        map.clear();
        map.put("command", "toast_short");
        boolean b = template.exists ? TemplateInfo.updateTemplate(template) : TemplateInfo.addTemplate(template);
        map.put("message", b ? "Шаблон изменен" : "Произошла ошибка при изменении шаблона");
        connection.send(new Container(getName(), map));
    }
}
