package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateUser extends CommandAbstract {
    public UpdateUser() {
        super("update_user");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        List<Groups> groups = new ArrayList<>();
        groups.add(Groups.unconfirmed);
        groups.add(Groups.user);
        groups.add(Groups.admin);
        User user = (User) map.get("user");
        map.clear();
        map.put("command", "toast_short");
        boolean b = groups.indexOf(user.group) > -1
                && !user.group.equals(Groups.developer)
                && (user.exists ? UserInfo.updateUser(user) : UserInfo.addUser(user));
        map.put("message", b ? "Пользователь изменен" : "Произошла ошибка при изменении пользователя");
        connection.send(map);
    }
}
