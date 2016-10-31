package ru.falseteam.schedule.server.socket.commands;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.UserInfo;

import java.util.List;
import java.util.Map;

import static ru.falseteam.schedule.server.Main.vk;
import static ru.falseteam.schedule.server.sql.Java2MySQL.addUser;
import static ru.falseteam.schedule.server.sql.Java2MySQL.getUserInfo;

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
            UserXtrCounters vk_user = users.get(0);

            UserInfo user = getUserInfo(vk_user.getId());
            if (user == null) throw new Exception("user info is null");
            if (user.isExists()) {
                permissions = user.getGroup().name();
            } else {
                user.setName(vk_user.getLastName() + " " + vk_user.getFirstName());
                user.setVk_token(token);
                user.setGroup(Connection.Groups.unconfirmed);
                if (!addUser(user)) {
                    Console.err("Can't add user with vk_id " + user.getVk_id());
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        map.clear();
        map.put("command", "auth");
        map.put("version", "1.1a");
        map.put("group", permissions);
        connection.send(map);
    }
}
