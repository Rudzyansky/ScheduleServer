package ru.falseteam.schedule.server.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.Schedule;
import ru.falseteam.schedule.server.StaticSettings;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Create connection to database.
 * Send all requests to database.
 *
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class SQLConnection {
    private static Logger log = LogManager.getLogger(SQLConnection.class.getName());

    private static Connection connection;
    private static Statement statement;

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

            WeekDayInfo.createTable();
            LessonNumberInfo.createTable();
            LessonInfo.createTable();
            TemplateInfo.createTable();

            WeekNumberInfo.createTable();
            JournalInfo.createTable();

//            Schedule.addPeriodicalTask(JournalInfo.addRec, new Date(), 24 * 60 * 60 * 1000);
            Schedule.addPeriodicalTask(JournalInfo.addRec, 24 * 60 * 60 * 1000);
        } catch (Exception e) {
            log.fatal("Can not load database, server will be stop", e);
            Main.stop();
        }
        log.trace("Database connected");
    }

    public static void stop() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            log.fatal("Can not close database connection", e);
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
            // if database already exist.
            return false;
        }
    }

    static PreparedStatement update(String table, String condition, String... columns) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE `").append(table).append("` SET `").append(columns[0]).append("` = ?");
        for (int i = 1; i < columns.length; ++i) sb.append(", `").append(columns[i]).append("` = ?");
        sb.append(' ').append(condition).append(" ;");
        return connection.prepareStatement(sb.toString());
    }

    static PreparedStatement insert(String table, String... columns) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `").append(table).append("` (").append('`').append(columns[0]).append('`');
        for (int i = 1; i < columns.length; ++i) sb.append(", `").append(columns[i]).append('`');
        sb.append(") VALUES ( ?");
        for (int i = 1; i < columns.length; ++i) sb.append(", ?");
        sb.append(");");
        return connection.prepareStatement(sb.toString());
    }
}
