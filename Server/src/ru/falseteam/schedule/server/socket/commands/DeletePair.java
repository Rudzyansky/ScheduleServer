package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Pair;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.PairInfo;

import java.util.Map;

public class DeletePair extends CommandAbstract {
    public DeletePair() {
        super("delete_pair");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        boolean b = PairInfo.deletePair((Pair) map.get("pair"));
        map.clear();
        map.put("command", "toast_short");
        map.put("message", b ? "Предмет удален" : "Произошла ошибка при удалении предмета");
        connection.send(map);
    }
}
