package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Pair;
import ru.falseteam.schedule.serializable.User;

import java.sql.ResultSet;
import java.util.ArrayList;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

public class UserInfo {

    private static ArrayList<User> users = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void loadFromBase() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users`;");
            users.clear();
            if (!rs.first()) return;
            do {
                User user = User.Factory.getDefault();
                user.exists = true;
                user.id = rs.getInt("id");
                user.name = rs.getString("name");
                user.vkId = rs.getInt("vk_id");
                user.group = Groups.valueOf(rs.getString("permissions"));
                users.add(user);
            } while (rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUser(final int vkId, User user) {
        try {
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

    public static User getUser(final String name) {
        try {
            User user = User.Factory.getDefault();
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `name` LIKE '" + name + "';");
            user.exists = rs.first();
            if (!user.exists) return null;
            user.id = rs.getInt("id");
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
            return rs.next() ? null : user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateName(final User user) {
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `name`='" + user.name + "'" +
                    " WHERE `id` LIKE '" + user.id + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateGroup(final User user) {
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `permissions`='" + user.group.name() + "'" +
                    " WHERE `id` LIKE '" + user.id + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateVkId(final User user) {
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `vk_id`='" + user.vkId + "'" +
                    " WHERE `id` LIKE '" + user.id + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
