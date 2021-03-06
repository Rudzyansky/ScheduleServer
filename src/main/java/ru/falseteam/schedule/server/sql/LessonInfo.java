package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.server.socket.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.falseteam.vframe.sql.SQLConnection.executeQuery;
import static ru.falseteam.vframe.sql.SQLConnection.executeUpdate;

/**
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class LessonInfo {

    static {
        Worker.getS().getSubscriptionManager().addEvent("GetLessons",
                LessonInfo::getLessonsForSubscriptions,
                Groups.developer, Groups.admin, Groups.user);
    }

    private static Map<String, Object> getLessonsForSubscriptions() {
        Map<String, Object> map = new HashMap<>();
        map.put("lessons", getLessons());
        return map;
    }

    private static void onLessonTableUpdate() {
        Worker.getS().getSubscriptionManager().onEventDataChange("GetLessons", getLessonsForSubscriptions());
    }

    /**
     * getLessons load table to variable
     *
     * @return {@link List<Lesson>}, null if SQLException
     */
    public static List<Lesson> getLessons() {
        try {
//            ResultSet rs = executeQuery("SELECT * FROM `lessons` ORDER BY `lesson_name`;");
            ResultSet rs = executeQuery("SELECT * FROM `lessons`;");
            List<Lesson> lessons = new ArrayList<>();
            if (!rs.first()) return lessons;
            do lessons.add(getLesson(rs));
            while (rs.next());
            return lessons;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * getLesson load lesson from table `lessons` using `lesson_id` field
     *
     * @param lesson_id - lesson_id in base
     * @return {@link Lesson} if exists in base, {null} if not exists or SQLException
     */
    public static Lesson getLesson(final int lesson_id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `lessons` WHERE `lesson_id` LIKE '" + lesson_id + "';");
            return rs.first() ? getLesson(rs) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param rs - {@link ResultSet}, из которого вытаскиваем инфу
     * @return {@link Lesson}, or throws SQLException
     */
    static Lesson getLesson(ResultSet rs) throws SQLException {
        Lesson lesson = Lesson.Factory.getDefault();
        lesson.exists = true;
        lesson.id = rs.getInt("lesson_id");
        lesson.name = rs.getString("lesson_name");
        lesson.audience = rs.getString("lesson_audience");
        lesson.teacher = rs.getString("lesson_teacher");
        lesson.lastTask = rs.getString("lesson_last_task");
        return lesson;
    }

    public static boolean updateLesson(final Lesson lesson) {
        try {
            executeUpdate("UPDATE `lessons` SET" +
                    " `lesson_type_id` = '" + lesson.type + "'," +
                    " `lesson_name` = '" + lesson.name + "'," +
                    " `lesson_audience` = '" + lesson.audience + "'," +
                    " `lesson_teacher` = '" + lesson.teacher + "'," +
                    " `lesson_last_task` = '" + lesson.lastTask + "'" +
                    " WHERE `lesson_id` LIKE '" + lesson.id + "';");
            onLessonTableUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean clearTask(final Lesson lesson) {
        try {
            executeUpdate("UPDATE `lessons` SET" +
                    " `lesson_last_task` = ''" +
                    " WHERE `lesson_id` LIKE '" + lesson.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteLesson(final Lesson lesson) {
        try {
            executeUpdate("DELETE FROM `lessons` WHERE `lesson_id` LIKE '" + lesson.id + "';");
            onLessonTableUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addLesson(final Lesson lesson) {
        try {
            executeUpdate("INSERT INTO `lessons` (`lesson_type_id`, `lesson_name`, `lesson_audience`,"+
                    " `lesson_teacher`, `lesson_last_task`) VALUES ('" +
                    lesson.type + "', '" +
                    lesson.name + "', '" +
                    lesson.audience + "', '" +
                    lesson.teacher + "', '" +
                    lesson.lastTask + "');");
            onLessonTableUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createTable() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `lessons` (" +
                    " `lesson_id` INT NOT NULL AUTO_INCREMENT," +
                    " `lesson_name` TEXT NOT NULL," +
                    " `lesson_audience` TEXT," +
                    " `lesson_teacher` TEXT," +
                    " `lesson_last_task` TEXT," +
                    " PRIMARY KEY (`lesson_id`)," +
                    " INDEX (`lesson_id`)," +
                    " UNIQUE (`lesson_id`)" +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
