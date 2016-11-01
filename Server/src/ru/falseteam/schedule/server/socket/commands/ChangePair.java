package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Pair;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.PairInfo;

import java.util.Map;

public class ChangePair extends CommandAbstract {
    public ChangePair() {
        super("change_pair");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        Pair pair = (Pair) map.get("pair");
        map.clear();
        map.put("command", "change_pair");
        boolean e = pair.isExists();
        boolean b = e ? PairInfo.updatePair(pair) : PairInfo.addPair(pair);
        map.put("result", b ? "complete" : "failed");
        connection.send(map);
    }
}
