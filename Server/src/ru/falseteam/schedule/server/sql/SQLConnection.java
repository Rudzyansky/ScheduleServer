package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.Schedule;
import ru.falseteam.schedule.server.StaticSettings;

import java.sql.*;
import java.util.TimerTask;

/**
 * Create connection to database.
 * Send all requests to database.
 *
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class SQLConnection {

    private static Connection connection;
    private static Statement statement;

    private static TimerTask ping = new TimerTask() {
        @Override
        public void run() {
            try {
                executeQuery("SELECT `lesson_number_id` FROM `lesson_numbers` WHERE `lesson_number_id` LIKE '1';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    public static void init() {
        try {
            connection = DriverManager
                    .getConnection(StaticSettings.getDbUrl(), StaticSettings.getDbUser(), StaticSettings.getDbPassword());
            statement = connection.createStatement();
            // Create database
            createDB();
            connection.setCatalog("schedule");
            statement = connection.createStatement();
            // Create tables
            UserInfo.createTable();
            LessonInfo.createTable();
            TemplateInfo.createTable();
        } catch (Exception e) {
            Console.err(e.getMessage());
            e.printStackTrace();
            Main.stop();
        }
        Console.print("DB connected");
        Schedule.addPeriodicalTask(ping, 60 * 60 * 1000);
    }

    public static void stop() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static ResultSet executeQuery(String request) throws SQLException {
        return statement.executeQuery(request);
    }

    static int executeUpdate(String request) throws SQLException {
        return statement.executeUpdate(request);
    }

    private static boolean createDB() {
        try {
            executeUpdate("CREATE DATABASE `schedule`;");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
