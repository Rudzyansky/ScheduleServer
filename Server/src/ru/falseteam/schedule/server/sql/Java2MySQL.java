package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.StaticSettings;
import ru.falseteam.schedule.server.socket.Connection.Groups;
import java.sql.*;
import java.sql.Connection;

public class Java2MySQL {

    private static Connection connection;
    private static Statement statement;

    public static void init() {
        try {
            connection = DriverManager
                    .getConnection(StaticSettings.getUrl(), StaticSettings.getUser(), StaticSettings.getPassword());
            statement = connection.createStatement();
        } catch (Exception e) {
            Console.err(e.getMessage());
            Main.stop();
        }
        Console.print("DB connected");
    }

    public static void stop() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException ignore) {
        }
    }

    public static ResultSet execute(String request) {
        try {
            return statement.executeQuery(request);
        } catch (SQLException ignore) {
            ignore.printStackTrace();
            return null;
        }
    }

    public static UserInfo getUserInfo(final int vk_id) {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setVk_id(vk_id);
            ResultSet rs = statement.executeQuery("SELECT * FROM `users` WHERE `vk_id` LIKE '" + vk_id + "';");
            userInfo.setExists(rs.first());
            if (!userInfo.isExists()) return userInfo;
            userInfo.setName(rs.getString("name"));
            String permissions = rs.getString("permissions");
            try {
                Groups.valueOf(rs.getString("permissions"));
            } catch (Exception ignore) {
                permissions = Groups.guest.name();
            }
            userInfo.setGroup(Groups.valueOf(permissions));
            userInfo.setVk_id(rs.getInt("vk_id"));
            userInfo.setVk_token(rs.getString("vk_token"));
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateToken(UserInfo userInfo) {
        try {
            statement.executeUpdate("UPDATE `users` " +
                    "SET `vk_token`='" + userInfo.getVk_token() + "' " +
                    "WHERE `vk_id`='" + userInfo.getVk_id() + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(final UserInfo userInfo) {
        try {
            statement.executeUpdate("INSERT INTO `users` (`vk_id`, `name`, `permissions`, `vk_token`)" +
                    " VALUES ('" + userInfo.getVk_id() + "', '" + userInfo.getName() + "', '" +
                    userInfo.getGroup() + "', '" + userInfo.getVk_token() + "');");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public static boolean addTableOfSchedule(final String name, final Map<String, String>[] map) {
//        try {
//            statement.executeUpdate("CREATE TABLE `schedule`.`" + name + "` ( `id` INT NOT NULL AUTO_INCREMENT ," +
//                    " `name` TEXT NULL , `audience` TEXT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
//            Thread.sleep(200);
//            for (Map<String, String> m : map) {
//                statement.executeUpdate("INSERT INTO `" + name + "` (`name`, `audience`)" +
//                        " VALUES ('" + m.get("name") + "', '" + m.get("audience") + "');");
//            }
//            return true;
//        } catch (Exception ignore) {
//            return false;
//        }
//    }
}
