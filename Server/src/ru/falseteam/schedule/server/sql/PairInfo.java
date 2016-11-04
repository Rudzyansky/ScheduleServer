package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

/**
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class PairInfo {

    /**
     * getPairs load table to variable
     *
     * @return {@link List<Pair>}, null if SQLException
     */
    public static List<Pair> getPairs() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `lessons` ORDER BY `name`;");
            List<Pair> pairs = new ArrayList<>();
            if (!rs.first()) return pairs;
            do {
                Pair pair = Pair.Factory.getDefault();
                pair.exists = true;
                pair.id = rs.getInt("lesson_id");
                pair.name = rs.getString("lesson_name");
                pair.audience = rs.getString("lesson_audience");
                pair.teacher = rs.getString("lesson_teacher");
                pair.lastTask = rs.getString("lesson_last_task");
                pairs.add(pair);
            } while (rs.next());
            return pairs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * getPair load lesson from table `lessons` using `lesson_id` field
     *
     * @param lesson_id - lesson_id in base
     * @return {@link Pair} if exists in base, {null} if not exists or SQLException
     */
    public static Pair getPair(final int lesson_id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `lessons` WHERE `lesson_id` LIKE '" + lesson_id + "';");
            if (!rs.first()) return null;
            Pair pair = Pair.Factory.getDefault();
            pair.exists = true;
            pair.id = rs.getInt("lesson_id");
            pair.name = rs.getString("lesson_name");
            pair.audience = rs.getString("lesson_audience");
            pair.teacher = rs.getString("lesson_teacher");
            pair.lastTask = rs.getString("lesson_last_task");
            return pair;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updatePair(final Pair pair) {
        try {
            executeUpdate("UPDATE `lessons` SET" +
                    " `lesson_name`='" + pair.name + "'," +
                    " `lesson_audience`='" + pair.audience + "'," +
                    " `lesson_teacher`='" + pair.teacher + "'," +
                    " `lesson_last_task`='" + pair.lastTask + "'" +
                    " WHERE `lesson_id` LIKE '" + pair.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletePair(final Pair pair) {
        try {
            executeUpdate("DELETE FROM `lessons` WHERE `lesson_id` LIKE '" + pair.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addPair(final Pair pair) {
        try {
            executeUpdate("INSERT INTO `lessons` (`lesson_name`, `lesson_audience`, `lesson_teacher`," +
                    " `lesson_last_task`) VALUES ('" +
                    pair.name + "', '" +
                    pair.audience + "', '" +
                    pair.teacher + "', '" +
                    pair.lastTask + "');");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean createTable() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `lessons` (" +
                    " `lesson_id` INT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    " `lesson_name` TEXT NOT NULL," +
                    " `lesson_audience` TEXT," +
                    " `lesson_teacher` TEXT," +
                    " `lesson_last_task` TEXT," +
                    " PRIMARY KEY (`lesson_id`)" +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
