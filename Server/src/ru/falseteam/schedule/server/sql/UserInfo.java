package ru.falseteam.schedule.server.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.falseteam.vframe.sql.SQLConnection.executeQuery;
import static ru.falseteam.vframe.sql.SQLConnection.executeUpdate;

/**
 * translator {@link User} to database using {SQLConnection}
 *
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class UserInfo {
    private static Logger log = LogManager.getLogger();

    /**
     * @return all user from database or null if throws internal exceptions.
     */
    public static List<User> getUsers() {
        try {
//            ResultSet rs = executeQuery("SELECT * FROM `users` ORDER BY `user_name`;");
            ResultSet rs = executeQuery("SELECT `user_id`, `user_name`, `user_at_list`, `user_vk_id`, `user_permissions` FROM `users` ORDER BY `user_at_list`;");
            List<User> users = new ArrayList<>();
            if (!rs.first()) return users; // если таблица пустая.
            do {
                User user = User.Factory.getDefault();
                user.exists = true;
                user.id = rs.getInt("user_id");
                user.name = rs.getString("user_name");
                user.atList = rs.getInt("user_at_list");
                user.permissions = Groups.valueOf(rs.getString("user_permissions"));
                user.vkId = rs.getInt("user_vk_id");
                users.add(user);
            }
            while (rs.next());
            return users;
        } catch (SQLException e) {
            log.error("Database error", e);
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
        } catch (SQLException e) {
            log.error("Database error", e);
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
        } catch (SQLException e) {
            log.error("Database error", e);
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
        } catch (SQLException e) {
            log.error("Database error", e);
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
        user.atList = rs.getInt("user_at_list");
        user.permissions = Groups.valueOf(rs.getString("user_permissions"));
        user.vkId = rs.getInt("user_vk_id");
        user.vkToken = rs.getString("user_vk_token");
        user.register = rs.getTimestamp("user_register");
        user.lastAuth = rs.getTimestamp("user_last_auth");
        user.sdkVersion = rs.getInt("user_sdk_version");
        user.appVersion = rs.getString("user_app_version");
        return user;
    }

    public static boolean updateUser(final User user) {
        if (!user.exists) throw new RuntimeException();
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `user_name` = '" + user.name + "'," +
                    " `user_permissions` = '" + user.permissions.name() + "'," +
                    " `user_vk_id` = '" + user.vkId + "'," +
                    " `user_vk_token` = '" + user.vkToken + "'," +
                    " `user_last_auth` = '" + user.lastAuth + "'," +
                    " `user_sdk_version` = '" + user.sdkVersion + "'," +
                    " `user_app_version` = '" + user.appVersion + "'" +
                    " WHERE `user_id` LIKE '" + user.id + "';");
            return true;
        } catch (SQLException e) {
            log.error("Database error", e);
            return false;
        }
    }

    public static boolean updateStat(final User user) {
        if (!user.exists) throw new RuntimeException();
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `user_vk_token` = '" + user.vkToken + "'," +
                    " `user_last_auth` = '" + user.lastAuth + "'," +
                    " `user_sdk_version` = '" + user.sdkVersion + "'," +
                    " `user_app_version` = '" + user.appVersion + "'" +
                    " WHERE `user_id` LIKE '" + user.id + "';");
            return true;
        } catch (SQLException e) {
            log.error("Database error", e);
            return false;
        }
    }

    public static boolean deleteUser(final User user) {
        try {
            executeUpdate("DELETE FROM `users` WHERE `user_id` LIKE '" + user.id + "';");
            return true;
        } catch (SQLException e) {
            log.error("Database error", e);
            return false;
        }
    }

    public static boolean addUser(final User user) {
        try {
            executeUpdate("INSERT INTO `users` (`user_name`, `user_vk_id`, `user_vk_token`, `user_permissions`," +
                    " `user_register`, `user_last_auth`, `user_sdk_version`, `user_app_version`)" +
                    " VALUES ('" +
                    user.name + "', '" +
                    user.vkId + "', '" +
                    user.vkToken + "', '" +
                    user.permissions.name() + "', '" +
                    user.register + "', '" +
                    user.lastAuth + "', '" +
                    user.sdkVersion + "', '" +
                    user.appVersion + "');");
            return true;
        } catch (SQLException e) {
            log.error("Database error", e);
            return false;
        }
    }

    public static boolean createTable() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `users` (" +
                    " `user_id` INT NOT NULL AUTO_INCREMENT," +
                    " `user_name` TEXT NOT NULL," +
                    " `group_id` INT NULL DEFAULT NULL," +
                    " `user_vk_id` INT NULL DEFAULT NULL," +
                    " `user_vk_token` TEXT," +
                    " `user_permissions` TEXT NOT NULL," +
                    " `user_register` DATETIME," +
                    " `user_last_auth` DATETIME," +
                    " `user_sdk_version` INT," +
                    " `user_app_version` TEXT," +
                    " PRIMARY KEY (`user_id`)," +
                    " INDEX (`user_id`)," +
                    " UNIQUE (`user_id`)," +
                    " UNIQUE (`user_vk_id`)," +
                    " KEY `group_id` (`group_id`)," +
                    " FOREIGN KEY (`group_id`) REFERENCES `groups`(`group_id`) ON DELETE RESTRICT ON UPDATE RESTRICT," +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (SQLException ignore) {
            // throw this if table already created
            return false;
        }
    }
}
