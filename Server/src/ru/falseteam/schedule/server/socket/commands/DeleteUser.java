package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
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
        User user = (User) map.get("user");
        map.clear();
        map.put("command", "toast_short");
        boolean b = !user.group.equals(Groups.developer) && UserInfo.deleteUser(user);
        map.put("message", b ? "Пользователь удален" : "Произошла ошибка при удалении пользователя");
        connection.send(map);
    }
}
