package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Lesson;

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
     * @return {@link List< Lesson >}, null if SQLException
     */
    public static List<Lesson> getPairs() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `lessons` ORDER BY `name`;");
            List<Lesson> lessons = new ArrayList<>();
            if (!rs.first()) return lessons;
            do {
                Lesson lesson = Lesson.Factory.getDefault();
                lesson.exists = true;
                lesson.id = rs.getInt("lesson_id");
                lesson.name = rs.getString("lesson_name");
                lesson.audience = rs.getString("lesson_audience");
                lesson.teacher = rs.getString("lesson_teacher");
                lesson.lastTask = rs.getString("lesson_last_task");
                lessons.add(lesson);
            } while (rs.next());
            return lessons;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * getPair load lesson from table `lessons` using `lesson_id` field
     *
     * @param lesson_id - lesson_id in base
     * @return {@link Lesson} if exists in base, {null} if not exists or SQLException
     */
    public static Lesson getPair(final int lesson_id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `lessons` WHERE `lesson_id` LIKE '" + lesson_id + "';");
            if (!rs.first()) return null;
            Lesson lesson = Lesson.Factory.getDefault();
            lesson.exists = true;
            lesson.id = rs.getInt("lesson_id");
            lesson.name = rs.getString("lesson_name");
            lesson.audience = rs.getString("lesson_audience");
            lesson.teacher = rs.getString("lesson_teacher");
            lesson.lastTask = rs.getString("lesson_last_task");
            return lesson;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updatePair(final Lesson lesson) {
        try {
            executeUpdate("UPDATE `lessons` SET" +
                    " `lesson_name`='" + lesson.name + "'," +
                    " `lesson_audience`='" + lesson.audience + "'," +
                    " `lesson_teacher`='" + lesson.teacher + "'," +
                    " `lesson_last_task`='" + lesson.lastTask + "'" +
                    " WHERE `lesson_id` LIKE '" + lesson.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletePair(final Lesson lesson) {
        try {
            executeUpdate("DELETE FROM `lessons` WHERE `lesson_id` LIKE '" + lesson.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addPair(final Lesson lesson) {
        try {
            executeUpdate("INSERT INTO `lessons` (`lesson_name`, `lesson_audience`, `lesson_teacher`," +
                    " `lesson_last_task`) VALUES ('" +
                    lesson.name + "', '" +
                    lesson.audience + "', '" +
                    lesson.teacher + "', '" +
                    lesson.lastTask + "');");
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
