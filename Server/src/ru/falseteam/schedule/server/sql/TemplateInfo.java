package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Template;

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
public class TemplateInfo {

    /**
     * getTemplates load table to variable
     *
     * @return {@link List<Template>}, null if SQLException
     */
    public static List<Template> getTemplates() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `templates` NATURAL JOIN (`week_days`, `lessons`)");
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
     * getTemplate load template from table `templates` using `id` field
     *
     * @param id - id in base
     * @return {@link Template} if exists in base, {null} if not exists or SQLException
     */
    public static Template getTemplate(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `template.weekDay.id` NATURAL JOIN (`week_days`, `lessons`)" +
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
        template.weekDay.id = rs.getInt("week_day_id");
        template.weekDay.name = rs.getString("week_day_name");
        template.lessonNumber.id = rs.getInt("lesson_number_id");
        template.lessonNumber.begin = rs.getTime("lesson_number_begin");
        template.lessonNumber.end = rs.getTime("lesson_number_end");
        template.lesson = LessonInfo.getLesson(rs);
        template.weekEvence = rs.getInt("week_evence");
        return template;
    }

    public static boolean updateTemplate(final Template template) {
        try {
            executeUpdate("UPDATE `templates` SET" +
                    " `week_day_id`='" + template.weekDay.id + "'," +
                    " `lesson_number_id`='" + template.lessonNumber.id + "'," +
                    " `lesson_id`='" + template.lesson.id + "'," +
                    " `week_evence`='" + template.weekEvence + "'" +
                    " WHERE `id` LIKE '" + template.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTemplate(final Template template) {
        try {
            executeUpdate("DELETE FROM `templates` WHERE `id` LIKE '" + template.id + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addTemplate(final Template template) {
        try {
            executeUpdate("INSERT INTO `templates` (`week_day_id`, `lesson_number_id`, `lesson_id`," +
                    " `week_evence`) VALUES ('" +
                    template.weekDay.id + "', '" +
                    template.lessonNumber.id + "', '" +
                    template.lesson.id + "', '" +
                    template.weekEvence + "');");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean createTable() {
        createTableWeekDays();
        createTableLessonNumbers();
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `templates` (" +
                    " `id` INT NOT NULL AUTO_INCREMENT," +
                    " `week_day_id` INT NOT NULL," +
                    " `lesson_number_id` INT NOT NULL," +
                    " `lesson_id` INT NOT NULL," +
                    " `week_evence` INT NOT NULL," +

                    " PRIMARY KEY (`id`)," +
                    " KEY `pair` (`lesson_id`)," +
                    " KEY `week_day_id` (`week_day_id`)," +
                    " KEY `lesson_number` (`lesson_number_id`)," +

                    " FOREIGN KEY (`week_day_id`) REFERENCES `week_days`(`week_day_id`) ON DELETE RESTRICT ON UPDATE RESTRICT," +
                    " FOREIGN KEY (`lesson_number_id`) REFERENCES `lesson_numbers`(`lesson_number_id`) ON DELETE RESTRICT ON UPDATE CASCADE," +
                    " FOREIGN KEY (`lesson_id`) REFERENCES `lessons`(`lesson_id`) ON DELETE RESTRICT ON UPDATE CASCADE" +
                    ") ENGINE=InnoDB;");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }

    private static boolean createTableLessonNumbers() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `lesson_numbers` (" +
                    " `lesson_number_id` INT NOT NULL," +
                    " `lesson_number_begin` TIME NOT NULL," +
                    " `lesson_number_end` TIME NOT NULL," +
                    " PRIMARY KEY (`lesson_number_id`)," +
                    " INDEX (`lesson_number_id`)," +
                    " UNIQUE KEY (`lesson_number_id`)" +
                    ") ENGINE=InnoDB;");
            executeUpdate("INSERT INTO `lesson_numbers` (`lesson_number_id`," +
                    " `lesson_number_begin`, `lesson_number_end`) VALUES" +
                    "(1, '09:00:00', '10:35:00')," +
                    "(2, '10:45:00', '12:20:00')," +
                    "(3, '12:50:00', '14:25:00')," +
                    "(4, '14:35:00', '16:10:00')," +
                    "(5, '16:20:00', '17:55:00')," +
                    "(6, '18:00:00', '19:35:00')," +
                    "(7, '19:45:00', '21:20:00');");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }

    private static boolean createTableWeekDays() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `week_days` (" +
                    " `week_day_id` INT NOT NULL," +
                    " `week_day_name` TEXT NOT NULL," +
                    " PRIMARY KEY (`week_day_id`)," +
                    " INDEX (`week_day_id`)," +
                    " UNIQUE KEY (`week_day_id`)" +
                    ") ENGINE=InnoDB;");
            executeUpdate("INSERT INTO `week_days` (`week_day_id`, `week_day_name`) VALUES" +
                    "(1, 'Понедельник')," +
                    "(2, 'Вторник')," +
                    "(3, 'Среда')," +
                    "(4, 'Четверг')," +
                    "(5, 'Пятница')," +
                    "(6, 'Суббота')," +
                    "(7, 'Воскресенье');");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
