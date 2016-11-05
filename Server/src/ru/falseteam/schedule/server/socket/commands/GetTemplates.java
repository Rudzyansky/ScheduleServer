package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.TemplateInfo;

import java.util.Map;

public class GetTemplates extends CommandAbstract {
    public GetTemplates() {
        super("get_templates");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.put("templates", TemplateInfo.getTemplates());
        connection.send(map);
    }
}
