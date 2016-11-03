package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

/**
 * Created by Prog on 31.10.16.
 */
public class PairInfo {

    /**
     * getPairs load table in variable
     *
     * @return {@link List<Pair>}, null if SQLException
     */
    public static List<Pair> getPairs() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `pairs` ORDER BY `name`;");
            List<Pair> pairs = new ArrayList<>();
            if (!rs.first()) return pairs;
            do {
                Pair pair = Pair.Factory.getDefault();
                pair.exists = true;
                pair.id = rs.getInt("id");
                pair.name = rs.getString("name");
                pair.audience = rs.getString("audience");
                pair.teacher = rs.getString("teacher");
                pair.lastTask = rs.getString("last_task");
                pairs.add(pair);
            } while (rs.next());
            return pairs;
        } catch (SQLException ignore) {
            return null;
        }
    }

    /**
     * getPair load lesson from table `pairs` using `id` field
     *
     * @param id - identificator in base
     * @return {@link Pair} if exists in base, {null} if not exists or SQLException
     */
    public static Pair getPair(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `pairs` WHERE `id` LIKE '" + id + "';");
            if (!rs.first()) return null;
            Pair pair = Pair.Factory.getDefault();
            pair.exists = true;
            pair.id = rs.getInt("id");
            pair.name = rs.getString("name");
            pair.audience = rs.getString("audience");
            pair.teacher = rs.getString("teacher");
            pair.lastTask = rs.getString("last_task");
            return pair;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updatePair(final Pair pair) {
        try {
            executeUpdate("UPDATE `pairs` SET" +
                    " `name`='" + pair.name + "'," +
                    " `audience`='" + pair.audience + "'," +
                    " `teacher`='" + pair.teacher + "'," +
                    " `last_task`='" + pair.lastTask + "'" +
                    " WHERE `id` LIKE '" + pair.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletePair(final Pair pair) {
        try {
            executeUpdate("DELETE FROM `pairs` WHERE `id` LIKE '" + pair.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addPair(final Pair pair) {
        try {
            executeUpdate("INSERT INTO `pairs` (`name`, `audience`, `teacher`, `last_task`) VALUES ('" +
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
            executeUpdate("CREATE TABLE `pairs` (" +
                    " `id` INT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    " `name` TEXT NOT NULL," +
                    " `audience` TEXT," +
                    " `teacher` TEXT," +
                    " `last_task` TEXT," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
