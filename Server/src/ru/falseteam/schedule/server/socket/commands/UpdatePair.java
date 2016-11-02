package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Pair;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.PairInfo;

import java.util.Map;

public class UpdatePair extends CommandAbstract {
    public UpdatePair() {
        super("update_pair");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        Pair pair = (Pair) map.get("pair");
        map.clear();
        map.put("command", "toast_short");
        boolean e = pair.exists;
        boolean b = e ? PairInfo.updatePair(pair) : PairInfo.addPair(pair);
        map.put("message", b ? "Предмет изменен" : "Произошла ошибка при изменении предмета");
        connection.send(map);
    }
}
