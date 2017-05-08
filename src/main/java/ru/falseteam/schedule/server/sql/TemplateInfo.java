package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.server.socket.Worker;
import ru.falseteam.vframe.sql.SQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.falseteam.vframe.sql.SQLConnection.executeQuery;
import static ru.falseteam.vframe.sql.SQLConnection.executeUpdate;

/**
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class TemplateInfo {

    static {
        Worker.getS().getSubscriptionManager().addEvent(
                "GetTemplates", TemplateInfo::getTemplatesForSubscriptions,
                Groups.developer, Groups.admin, Groups.user, Groups.unconfirmed);
    }

    static final String table = "templates";

    private static Map<String, Object> getTemplatesForSubscriptions() {
        Map<String, Object> map = new HashMap<>();
        map.put("templates", getTemplates());
        return map;
    }

    private static void onDataUpdate() {
        Worker.getS().getSubscriptionManager().onEventDataChange("GetTemplates", getTemplatesForSubscriptions());
    }

    /**
     * getTemplates load table to variable
     *
     * @return {@link List<Template>}, null if SQLException
     */
    public static List<Template> getTemplates() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `templates`" +
                    " NATURAL JOIN (`week_days`, `lesson_numbers`, `lessons`)" +
                    " ORDER BY `week_day_id`, `lesson_number_id`, `weeks`;");
            List<Template> templates = new ArrayList<>();
            if (!rs.first()) return templates;
            do templates.add(getTemplate(rs));
            while (rs.next());
            return templates;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * getRecord load template from table `templates` using `id` field
     *
     * @param id - id in base
     * @return {@link Template} if exists in base, {null} if not exists or SQLException
     */
    public static Template getTemplate(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `" + table + "`" +
                    " NATURAL JOIN (`week_days`, `lesson_numbers`, `lessons`)" +
                    " WHERE `id` LIKE '" + id + "';");
            return rs.first() ? getTemplate(rs) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param rs - {@link ResultSet}, из которого вытаскиваем инфу
     * @return {@link Template} if exists in base, {null} if not exists or SQLException
     */
    private static Template getTemplate(final ResultSet rs) throws SQLException {
        Template template = Template.Factory.getDefault();
        template.exists = true;
        template.id = rs.getInt("id");
        template.weekDay = WeekDayInfo.getWeekDay(rs);
        template.lessonNumber = LessonNumberInfo.getLessonNumber(rs);
        template.lesson = LessonInfo.getLesson(rs);
        template.weeks = BitSet.valueOf(rs.getBytes("weeks"));
        return template;
    }

    public static boolean updateTemplate(final Template template) {
        try {
            String condition = "WHERE `id` LIKE '" + template.id + "'";
            PreparedStatement s = SQLConnection.update(table, condition,
                    "week_day_id", "lesson_number_id", "lesson_id", "weeks");
            s.setInt(1, template.weekDay.id);
            s.setInt(2, template.lessonNumber.id);
            s.setInt(3, template.lesson.id);
            s.setBytes(4, template.weeks.toByteArray());
            s.execute();
            onDataUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTemplate(final Template template) {
        try {
            executeUpdate("DELETE FROM `templates` WHERE `id` LIKE '" + template.id + "';");
            onDataUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addTemplate(final Template template) {
        try {
            PreparedStatement s = SQLConnection.insert(table,
                    "week_day_id", "lesson_number_id", "lesson_id", "weeks");
            s.setInt(1, template.weekDay.id);
            s.setInt(2, template.lessonNumber.id);
            s.setInt(3, template.lesson.id);
            s.setBytes(4, template.weeks.toByteArray());
            s.execute();
            onDataUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createTable() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `templates` (" +
                    " `id` INT NOT NULL AUTO_INCREMENT," +
                    " `week_day_id` INT NOT NULL," +
                    " `lesson_number_id` INT NOT NULL," +
                    " `lesson_id` INT NOT NULL," +
                    " `weeks` BINARY(4) NOT NULL," +

                    " PRIMARY KEY (`id`)," +
                    " KEY `week_day_id` (`week_day_id`)," +
                    " KEY `lesson_number_id` (`lesson_number_id`)," +
                    " KEY `lesson_id` (`lesson_id`)," +

                    " FOREIGN KEY (`week_day_id`) REFERENCES `week_days`(`week_day_id`) ON DELETE RESTRICT ON UPDATE RESTRICT," +
                    " FOREIGN KEY (`lesson_number_id`) REFERENCES `lesson_numbers`(`lesson_number_id`) ON DELETE RESTRICT ON UPDATE CASCADE," +
                    " FOREIGN KEY (`lesson_id`) REFERENCES `lessons`(`lesson_id`) ON DELETE RESTRICT ON UPDATE CASCADE" +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
