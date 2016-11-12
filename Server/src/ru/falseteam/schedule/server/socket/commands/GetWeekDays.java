package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.WeekDayInfo;

import java.util.Map;

public class GetWeekDays extends CommandAbstract {
    public GetWeekDays() {
        super("get_week_days");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.put("week_days", WeekDayInfo.getWeekDays());
        connection.send(map);
    }
}
