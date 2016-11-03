package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Pair;
import ru.falseteam.schedule.serializable.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

public class UserInfo {

    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users` ORDER BY `name`;");
            if (!rs.first()) return users;
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
        return users;
    }

    /**
     * @param id - id in base
     * @return {@link User} if user exists, null if user not exists or SQLException
     */
    public static User getUserFromID(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `id` LIKE '" + id + "';");
            if (!rs.first()) return null;
            User user = User.Factory.getDefault();
            user.exists = true;
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
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param vkId - vk id
     * @return {@link User} if user exists, null if user not exists or SQLException
     */
    public static User getUser(final int vkId) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `vk_id` LIKE '" + vkId + "';");
            if (!rs.first()) return null;
            User user = User.Factory.getDefault();
            user.exists = true;
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
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param name - family and name
     * @return {@link User} if user exists, null if user not exists or SQLException
     */
    public static User getUser(final String name) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `name` LIKE '" + name + "';");
            if (!rs.first()) return null;
            User user = User.Factory.getDefault();
            user.exists = true;
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

    public static boolean updateUser(final User user) {
        if (!user.exists) throw new RuntimeException();
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `name`='" + user.name + "'," +
                    " `permissions`='" + user.group.name() + "'," +
                    " `vk_id`='" + user.vkId + "'," +
                    " `vk_token`='" + user.vkToken + "'" +
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

    public static boolean deleteUser(final User user) {
        try {
            executeUpdate("DELETE FROM `users` WHERE `id` LIKE '" + user.id + "';");
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
