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

import static ru.falseteam.schedule.serializable.Groups.user;
import static ru.falseteam.schedule.server.Main.vk;
import static ru.falseteam.schedule.server.StaticSettings.getLastClientVersion;
import static ru.falseteam.schedule.serializable.Groups.*;

public class Auth extends CommandAbstract {
    public Auth() {
        super("auth");
    }

    @Override
    public void exec(Connection connection, Map<String, Object> map) {
        User user;
        try {
            //Проверяем токен на валидность.
            String token = (String) map.get("token");
            UserActor actor = new UserActor(0, token);
            List<UserXtrCounters> users = vk.users().get(actor).fields(UserField.PHOTO_50).execute();
            if (users.isEmpty()) throw new Exception("Vk response is empty");
            UserXtrCounters vkUser = users.get(0);

            // Получаем юзера из бд если он существует.
            user = UserInfo.getUser(vkUser.getId());
            if (user == null) {
                // Если юзер не существует.
                user = User.Factory.getDefault();
                user.vkId = vkUser.getId();
                user.name = vkUser.getLastName() + " " + vkUser.getFirstName();
                user.vkToken = token;
                user.group = unconfirmed;
                if (!UserInfo.addUser(user)) throw new Exception("Can't add user with vk_id " + user.vkId);
                user.exists = true; // Вот тут хз мб это нужно в методе бд делать.
            } else {
                // Если юзер существует.
                if (!user.vkToken.equals(token)) {
                    // Обновляем токен если он поменялся.
                    user.vkToken = token;
                    UserInfo.updateToken(user);
                }
            }
            connection.setUser(user);
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return;
        }

        // Формируем ответ пользователю.
        map.clear();
        map.put("command", "auth");
        map.put("version", getLastClientVersion());
        map.put("group", user.group.name());
        connection.send(map);
    }
}
