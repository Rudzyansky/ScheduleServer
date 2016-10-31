package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.StaticSettings;

import java.sql.*;
import java.sql.Connection;

public class SQLConnection {

    private static Connection connection;
    private static Statement statement;

    public static void init() {
        try {
            connection = DriverManager
                    .getConnection(StaticSettings.getUrl(), StaticSettings.getUser(), StaticSettings.getPassword());
            statement = connection.createStatement();
            UserInfo.createTable();
            PairInfo.createTable();
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

    static ResultSet executeQuery(String request) throws SQLException {
        return statement.executeQuery(request);
    }

    static int executeUpdate(String request) throws SQLException {
        return statement.executeUpdate(request);
    }
}
