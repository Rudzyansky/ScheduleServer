package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;

import java.sql.ResultSet;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

public class UserInfo {

    public static User getUser(final int vkId) {
        try {
            User user = new User();
            user.vkId = vkId;
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `vk_id` LIKE '" + vkId + "';");
            user.exists = rs.first();
            if (!user.exists) return user;
            user.name = rs.getString("name");
            String permissions = rs.getString("permissions");
            try {
                Groups.valueOf(rs.getString("permissions"));
            } catch (Exception ignore) {
                permissions = Groups.guest.name();
            }
            user.group = Groups.valueOf(permissions);
            user.vkId = rs.getInt("vk_id");
            user.vkToken = rs.getString("vk_token");
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateToken(final User user) {
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `vk_token`='" + user.vkToken + "'" +
                    " WHERE `vk_id` LIKE '" + user.vkId + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(final User user) {
        try {
            executeUpdate("INSERT INTO `users` (`name`, `vk_id`, `vk_token`, `permissions`) VALUES ('" +
                    user.name + "', '" +
                    user.vkId + "', '" +
                    user.vkToken + "', '" +
                    user.group + "');");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean createTable() {
        try {
            executeUpdate("CREATE TABLE `users` (" +
                    " `id` INT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    " `name` TEXT NOT NULL," +
                    " `vk_id` INT UNSIGNED NULL DEFAULT NULL," +
                    " `vk_token` TEXT," +
                    " `permissions` TEXT NOT NULL," +
                    " PRIMARY KEY (`id`)," +
                    " UNIQUE KEY `vk_id` (`vk_id`)" +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }
}
