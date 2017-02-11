package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.sql.WeekDayInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class GetWeekDays extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        Container c = new Container(getName(), true);
        c.data.put("week_days", WeekDayInfo.getWeekDays());
        connection.send(c);
    }
}
