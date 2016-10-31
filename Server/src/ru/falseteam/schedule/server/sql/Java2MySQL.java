package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.StaticSettings;

import java.sql.*;

public class Java2MySQL {
    //private static final String url = "jdbc:mysql://localhost:3306/schedule";
    //private static final String user = "scheduleServer";
    //private static final String pass = "Et11ZGz9LPwmwpf0";

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

    public static boolean existsUser(final int vk_id) throws Exception {
        try {
            return statement.executeQuery("SELECT * FROM `users` WHERE `vk_id` LIKE '" + vk_id + "';").first();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("DB error");
        }
    }

    public static String getPermissions(final int vk_id) {
        try {
            ResultSet rs = statement.executeQuery("SELECT `permissions` FROM `users` WHERE `vk_id` LIKE '" + vk_id + "';");
            rs.first();
            return rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addUser(final int vk_id, final String name, final String group, final String vk_token) {
        try {
            statement.executeUpdate("INSERT INTO `users` (`vk_id`, `name`, `permissions`, `vk_token`)" +
                    " VALUES ('" + vk_id + "', '" + name + "', '" + group + "', '" + vk_token + "');");
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
