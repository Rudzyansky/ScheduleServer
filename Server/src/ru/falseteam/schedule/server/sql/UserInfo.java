package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.server.socket.Connection;

import java.sql.ResultSet;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

public class UserInfo {

    public static class User {
        private boolean exists = false;
        private String name;
        private String vkToken;
        private int vkId;
        private Connection.Groups group;

        public boolean isExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVkToken() {
            return vkToken;
        }

        public void setVkToken(String vkToken) {
            this.vkToken = vkToken;
        }

        public int getVkId() {
            return vkId;
        }

        public void setVkId(int vkId) {
            this.vkId = vkId;
        }

        public Connection.Groups getGroup() {
            return group;
        }

        public void setGroup(Connection.Groups group) {
            this.group = group;
        }
    }

    public static User getUser(final int vkId) {
        try {
            User user = new User();
            user.setVkId(vkId);
            ResultSet rs = executeQuery("SELECT * FROM `users` WHERE `vk_id` LIKE '" + vkId + "';");
            user.setExists(rs.first());
            if (!user.isExists()) return user;
            user.setName(rs.getString("name"));
            String permissions = rs.getString("permissions");
            try {
                Connection.Groups.valueOf(rs.getString("permissions"));
            } catch (Exception ignore) {
                permissions = Connection.Groups.guest.name();
            }
            user.setGroup(Connection.Groups.valueOf(permissions));
            user.setVkId(rs.getInt("vk_id"));
            user.setVkToken(rs.getString("vk_token"));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateToken(final User user) {
        try {
            executeUpdate("UPDATE `users` SET" +
                    " `vk_token`='" + user.getVkToken() + "'" +
                    " WHERE `vk_id` LIKE '" + user.getVkId() + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(final User user) {
        try {
            executeUpdate("INSERT INTO `users` (`name`, `vk_id`, `vk_token`, `permissions`) VALUES ('" +
                    user.getName() + "', '" +
                    user.getVkId() + "', '" +
                    user.getVkToken() + "', '" +
                    user.getGroup() + "');");
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
