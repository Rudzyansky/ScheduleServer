package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Pair;

import java.sql.ResultSet;
import java.util.ArrayList;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

/**
 * Created by Prog on 31.10.16.
 */
public class PairInfo {

    private static ArrayList<Pair> pairs = new ArrayList<>();

    public static ArrayList<Pair> getPairs() {
        return pairs;
    }

    public static int count() {
        return pairs.size();
    }

    public static void loadFromBase() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `pairs`;");
            pairs.clear();
            if (!rs.first()) return;
            do {
                Pair pair = new Pair();
                pair.exists = true;
                pair.id = rs.getInt("id");
                pair.name = rs.getString("name");
                pair.audience = rs.getString("audience");
                pair.teacher = rs.getString("teacher");
                pair.lastTask = rs.getString("last_task");
                pairs.add(pair);
            } while (rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Pair getPair(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `pairs` WHERE `id` LIKE '" + id + "';");
            Pair pair = new Pair();
            pair.exists = rs.first();
            if (!pair.exists) return pair;
            pairs.clear();
            pair.id = rs.getInt("id");
            pair.name = rs.getString("name");
            pair.audience = rs.getString("audience");
            pair.teacher = rs.getString("teacher");
            pair.lastTask = rs.getString("last_task");
            return pair;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updatePair(Pair pair) {
        try {
            executeUpdate("UPDATE `pairs` SET" +
                    " `name`='" + pair.name + "'," +
                    " `audience`='" + pair.audience + "'," +
                    " `teacher`='" + pair.teacher + "'," +
                    " `last_task`='" + pair.lastTask + "'" +
                    " WHERE `id` LIKE '" + pair.id + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletePair(int id) {
        try {
            executeUpdate("DELETE FROM `pairs` WHERE `id` LIKE '" + id + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addPair(Pair pair) {
        try {
            executeUpdate("INSERT INTO `pairs` (`name`, `audience`, `teacher`, `last_task`) VALUES ('" +
                    pair.name + "', '" +
                    pair.audience + "', '" +
                    pair.teacher + "', '" +
                    pair.lastTask + "');");
            return true;
        } catch (Exception e) {
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
        } catch (Exception ignore) {
            return false;
        }
    }
}
