package ru.falseteam.schedule.server.socket.commands;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ru.falseteam.schedule.serializable.Groups.unconfirmed;
import static ru.falseteam.schedule.server.Main.vk;
import static ru.falseteam.schedule.server.StaticSettings.getLastClientVersion;

public class Auth extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        User user;
        try {
            //Проверяем токен на валидность.
            String token = (String) map.get("token");
            UserActor actor = new UserActor(0, token);
            List<UserXtrCounters> users = vk.users().get(actor).fields(UserField.PHOTO_50).execute();
            if (users.isEmpty()) throw new Exception("Vk response is empty");
            UserXtrCounters vkUser = users.get(0);

            int sdkVersion = (int) map.get("sdk_version");
            String appVersion = (String) map.get("app_version");
            Timestamp currentTime = new Timestamp(new Date().getTime());

            // Получаем юзера из бд если он существует.
            user = UserInfo.getUser(vkUser.getId());
            if (user == null) {
                // Если юзер не существует.
                user = User.Factory.getDefault();
                user.vkId = vkUser.getId();
                user.name = vkUser.getLastName() + " " + vkUser.getFirstName();
                user.vkToken = token;
                user.permissions = unconfirmed;
                user.register = currentTime;
                user.lastAuth = currentTime;
                user.sdkVersion = sdkVersion;
                user.appVersion = appVersion;
                if (!UserInfo.addUser(user)) throw new Exception("Can't add user with vk_id " + user.vkId);
                user.exists = true; // Вот тут хз мб это нужно в методе бд делать.
            } else {
                // Если юзер существует.
                // Обновляем стату
                user.vkToken = token;
                user.lastAuth = currentTime;
                user.sdkVersion = sdkVersion;
                user.appVersion = appVersion;
                UserInfo.updateStat(user);
            }
            ((Connection) connection).setUser(user);
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return;
        }

        // Формируем ответ пользователю.
        map.clear();
        map.put("command", "auth");
        map.put("version", getLastClientVersion());
        map.put("permissions", user.permissions.name());
        connection.send(new Container(getName(), map));
    }
}
