package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.StaticSettings;

import java.sql.*;
import java.sql.Connection;

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

    public static void init() {
        try {
            connection = DriverManager
                    .getConnection(StaticSettings.getUrl(), StaticSettings.getUser(), StaticSettings.getPassword());
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
}
