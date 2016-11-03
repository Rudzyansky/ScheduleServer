package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.PairInfo;

import java.util.Map;

public class GetPairs extends CommandAbstract {
    public GetPairs() {
        super("get_pairs");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        map.put("pairs", PairInfo.getPairs());
        connection.send(map);
    }
}
