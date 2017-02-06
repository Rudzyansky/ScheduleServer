package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.Map;

public class DeleteUser extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        User user = (User) map.get("user");
        User inBase = UserInfo.getUserFromID(user.id);
        map.clear();
        map.put("command", "toast_short");
        boolean b = inBase != null
                && !user.permissions.equals(Groups.developer)
                && !inBase.permissions.equals(Groups.developer)
                && UserInfo.deleteUser(user);
        // TODO: 05.02.17 fix it l
//        if (b) Worker.getClients().stream().filter(c -> c.getUser().id == user.id).forEach(Connection::disconnect);
        map.put("message", b ? "Пользователь удален" : "Произошла ошибка при удалении пользователя");
        connection.send(new Container(getName(), map));
    }
}
