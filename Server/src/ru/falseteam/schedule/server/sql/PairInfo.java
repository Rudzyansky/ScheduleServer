package ru.falseteam.schedule.server.sql;

import java.sql.ResultSet;
import java.util.ArrayList;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeQuery;
import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

/**
 * Created by Prog on 31.10.16.
 */
public class PairInfo {

    private static class Pair {
        private boolean exists = false;
        private int id;
        private String name;
        private String audience;
        private String teacher;
        private String lastTask;

        public boolean isExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getLastTask() {
            return lastTask;
        }

        public void setLastTask(String lastTask) {
            this.lastTask = lastTask;
        }
    }

    private static ArrayList<Pair> pairs;

    public static void loadFromBase() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `pairs`;");
            pairs.clear();
            while (!rs.next()) {
                Pair pair = new Pair();
                pair.setExists(true);
                pair.setId(rs.getInt("id"));
                pair.setName(rs.getString("name"));
                pair.setAudience(rs.getString("audience"));
                pair.setTeacher(rs.getString("teacher"));
                pair.setLastTask(rs.getString("last_task"));
                pairs.add(pair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Pair getPair(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `pairs` WHERE `id` LIKE '" + id + "';");
            Pair pair = new Pair();
            pair.setExists(rs.first());
            if (!pair.isExists()) return pair;
            pairs.clear();
            pair.setId(rs.getInt("id"));
            pair.setName(rs.getString("name"));
            pair.setAudience(rs.getString("audience"));
            pair.setTeacher(rs.getString("teacher"));
            pair.setLastTask(rs.getString("last_task"));
            return pair;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updatePair(Pair pair) {
        try {
            executeUpdate("UPDATE `pairs` SET" +
                    " `name`='" + pair.getName() + "'," +
                    " `audience`='" + pair.getAudience() + "'," +
                    " `teacher`='" + pair.getTeacher() + "'," +
                    " `last_task`='" + pair.getLastTask() + "'" +
                    " WHERE `id` LIKE '" + pair.getId() + "';");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addPair(Pair pair) {
        try {
            executeUpdate("INSERT INTO `pairs` (`name`, `audience`, `teacher`, `last_task`) VALUES (" +
                    pair.getName() + "', '" +
                    pair.getAudience() + "', '" +
                    pair.getTeacher() + "', '" +
                    pair.getLastTask() + "');");
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
