package ru.falseteam.schedule.server.socket.commands;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.socket.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.schedule.serializable.User;

import java.util.List;
import java.util.Map;

import static ru.falseteam.schedule.server.Main.vk;
import static ru.falseteam.schedule.server.StaticSettings.getLastClientVersion;
import static ru.falseteam.schedule.serializable.Groups.*;

public class Auth extends CommandAbstract {
    public Auth() {
        super("auth");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        String permissions = Groups.guest.name();
        try {
            String token = (String) map.get("token");
            UserActor actor = new UserActor(0, token);
            List<UserXtrCounters> users = vk.users().get(actor).fields(UserField.PHOTO_50).execute();
            if (users.isEmpty()) throw new Exception("response is empty");
            UserXtrCounters vk_user = users.get(0);

            User user = connection.getUser();
            UserInfo.getUser(vk_user.getId(), user);
            if (user == null) throw new Exception("user info is null");
            if (user.exists) {
                if (!user.vkToken.equals(token)) {
                    user.vkToken = token;
                    UserInfo.updateToken(user);
                }
                permissions = user.group.name();
            } else {
                user.name = vk_user.getLastName() + " " + vk_user.getFirstName();
                user.vkToken = token;
                user.group = unconfirmed;
                if (!UserInfo.addUser(user)) {
                    Console.err("Can't add user with vk_id " + user.vkId);
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        connection.getUser().group = Groups.valueOf(permissions);
        map.clear();
        map.put("command", "auth");
        map.put("version", getLastClientVersion());
        map.put("group", permissions);
        connection.send(map);
    }
}
