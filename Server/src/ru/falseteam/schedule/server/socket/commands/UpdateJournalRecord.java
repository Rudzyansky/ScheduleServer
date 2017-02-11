package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.server.sql.JournalInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class UpdateJournalRecord extends ProtocolAbstract {

    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        JournalRecord record = (JournalRecord) map.get("journal_record");
        boolean b = JournalInfo.updatePresented(record);
        Container c = new Container("ToastShort", true);
        c.data.put("message", b ? "Запись журнала изменена" : "Произошла ошибка при изменении записи журнала");
        connection.send(c);
    }
}
