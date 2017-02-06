package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.server.sql.JournalInfo;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ServerConnectionAbstract;
import ru.falseteam.vframe.socket.ServerProtocolAbstract;

import java.util.Map;

public class UpdateJournalRecord extends ServerProtocolAbstract {

    @Override
    public void exec(Map<String, Object> map, ServerConnectionAbstract connection) {
        JournalRecord record = (JournalRecord) map.get("journal_record");
        map.clear();
        map.put("command", "toast_short");
        boolean b = JournalInfo.updateWas(record);
        map.put("message", b ? "Запись журнала изменена" : "Произошла ошибка при изменении записи журнала");
        connection.send(new Container(getName(), map));
    }
}
