package ru.falseteam.schedule.server.socket.commands;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.Java2MySQL;

import java.util.List;
import java.util.Map;

import static ru.falseteam.schedule.server.Main.vk;

public class Auth extends CommandAbstract {
    public Auth() {
        super("auth");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        String permissions = Connection.Groups.guest.name();
        try {
            String token = (String) map.get("token");
            UserActor actor = new UserActor(0, token);
            List<UserXtrCounters> users = vk.users().get(actor).fields(UserField.PHOTO_50).execute();
            if (users.isEmpty()) throw new Exception("response is empty");
            UserXtrCounters user = users.get(0);
            int id = user.getId();
            String name = user.getLastName() + " " + user.getFirstName();
            if (Java2MySQL.existsUser(id)) {
                permissions = Java2MySQL.getPermissions(id);
                try {
                    Connection.Groups.valueOf(permissions);
                } catch (Exception e) {
                    Console.err("Permissions '" + permissions + "' not found");
                    e.printStackTrace();
                    permissions = Connection.Groups.guest.name();
                }
            } else if (!Java2MySQL.addUser(id, name, connection.currentGroup.name(), token))
                System.out.println("not add user with id " + id);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        map.clear();
        map.put("command", "auth");
        map.put("version", "0.1b");
        map.put("group", permissions);
        connection.send(map);
    }
}
