package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

/**
 * translator {@link User} to database using {@link SQLConnection}
 *
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class UserInfo {

    /**
     * @return all user from database or null if throws internal exceptions.
     */
    public static List<User> getUsers() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users` ORDER BY `user_name`;");
            List<User> users = new ArrayList<>();
            if (!rs.first()) return users; // если таблица пустая.
            do users.add(getUser(rs));
            while (rs.next());
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param id - id in base
     * @return {@link User} if user exists, null if user not exists or SQLException
     */
    public static User getUserFromID(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `user_id` LIKE '" + id + "';");
            return rs.first() ? getUser(rs) : null;
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
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `user_vk_id` LIKE '" + vkId + "';");
            return rs.first() ? getUser(rs) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param name - last name and first name
     * @return {@link User} if user exists, null if user not exists or SQLException
     */
    public static User getUser(final String name) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `user_name` LIKE '" + name + "';");
            return (!rs.first() || !rs.isLast()) ? null : getUser(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param rs - {@link ResultSet}, из которого вытаскиваем инфу
     * @return {@link User} if user exists, null if user not exists or SQLException
     */
    private static User getUser(final ResultSet rs) throws SQLException {
        User user = User.Factory.getDefault();
        user.exists = true;
        user.id = rs.getInt("user_id");
        user.name = rs.getString("user_name");
        user.group = Groups.valueOf(rs.getString("user_permissions"));
        user.vkId = rs.getInt("user_vk_id");
        user.vkToken = rs.getString("user_vk_token");
        return user;
    }

    public static boolean updateUser(final User user) {
        if (!user.exists) throw new RuntimeException();
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `user_name`='" + user.name + "'," +
                    " `user_permissions`='" + user.group.name() + "'," +
                    " `user_vk_id`='" + user.vkId + "'," +
                    " `user_vk_token`='" + user.vkToken + "'" +
                    " WHERE `user_id` LIKE '" + user.id + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateToken(final User user) {
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `user_vk_token`='" + user.vkToken + "'" +
                    " WHERE `user_vk_id` LIKE '" + user.vkId + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(final User user) {
        try {
            executeUpdate("DELETE FROM `users` WHERE `user_id` LIKE '" + user.id + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(final User user) {
        try {
            executeUpdate("INSERT INTO `users` (`user_name`, `user_vk_id`, `user_vk_token`, `user_permissions`)" +
                    " VALUES ('" +
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
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `users` (" +
                    " `user_id` INT NOT NULL AUTO_INCREMENT," +
                    " `user_name` TEXT NOT NULL," +
                    " `user_vk_id` INT NULL DEFAULT NULL," +
                    " `user_vk_token` TEXT," +
                    " `user_permissions` TEXT NOT NULL," +
                    " PRIMARY KEY (`user_id`)," +
                    " INDEX (`user_id`)," +
                    " UNIQUE KEY (`user_id`)," +
                    " UNIQUE KEY (`user_vk_id`)" +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }
}
