package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.JournalInfo;

import java.util.Map;

public class GetJournal extends CommandAbstract {
    public GetJournal() {
        super("get_journal");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.put("journal", JournalInfo.getJournal());
        connection.send(map);
    }
}
