package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.JournalInfo;

import java.util.Map;

public class UpdateJournalRecord extends CommandAbstract {
    public UpdateJournalRecord() {
        super("update_journal_record");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        JournalRecord record = (JournalRecord) map.get("journal_record");
        map.clear();
        map.put("command", "toast_short");
        boolean b = JournalInfo.updateWas(record);
        map.put("message", b ? "Запись журнала изменена" : "Произошла ошибка при изменении записи журнала");
        connection.send(map);
    }
}
