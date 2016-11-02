package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.UserInfo;

import java.util.Map;

public class DeleteUser extends CommandAbstract {
    public DeleteUser() {
        super("delete_user");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        boolean b = UserInfo.deleteUser((Integer) map.get("id"));
        map.clear();
        map.put("command", "toast_short");
        map.put("message", b ? "Пользователь удален" : "Произошла ошибка при удалении пользователя");
        connection.send(map);
    }
}
