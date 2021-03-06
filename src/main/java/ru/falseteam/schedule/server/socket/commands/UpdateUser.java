package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.socket.Worker;
import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateUser extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        List<Groups> groups = new ArrayList<>();
        groups.add(Groups.unconfirmed);
        groups.add(Groups.user);
        groups.add(Groups.admin);
        User user = (User) map.get("user");
        // TODO: 09.02.17 мегакостыль орундий-256
        user.permissions = Groups.valueOf((String) map.get("group"));
        User inBase = user.exists ? UserInfo.getUserFromID(user.id) : user;
        boolean b = inBase != null
                && groups.indexOf(inBase.permissions) > -1
                && groups.indexOf(user.permissions) > -1
                && !user.permissions.equals(Groups.developer)
                && (user.exists ? UserInfo.updateUser(user) : UserInfo.addUser(user));
        if (b) Worker.getClients().stream()
                .filter(c -> ((Connection) c).getUser().id == user.id)
                .forEach(c -> ((Connection) c).disconnect());
//                .forEach(c -> c.send(Auth.getSetPermission(user.permissions)));
        Container c = new Container("ToastShort", true);
        c.data.put("message", b ? "Пользователь изменен" : "Произошла ошибка при изменении пользователя");
        connection.send(c);
    }
}
