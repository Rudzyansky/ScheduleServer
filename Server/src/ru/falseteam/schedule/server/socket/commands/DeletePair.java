package ru.falseteam.schedule.server.socket.commands;

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
        boolean b = PairInfo.deletePair((Integer) map.get("id"));
        map.clear();
        map.put("command", "change_pair");
        map.put("result", b ? "Предмет удален" : "Произошла ошибка при удалении предмета");
        connection.send(map);
    }
}
