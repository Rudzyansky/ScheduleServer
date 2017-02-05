package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.WeekDay;

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
public class WeekDayInfo {
    public static final String table = "week_days";
    public static List<WeekDay> getWeekDays() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `week_days`");
            List<WeekDay> weekDays = new ArrayList<>();
            if (!rs.first()) return weekDays;
            do weekDays.add(getWeekDay(rs));
            while (rs.next());
            return weekDays;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static WeekDay getWeekDay(ResultSet rs) throws SQLException {
        WeekDay weekDay = new WeekDay();
        weekDay.id = rs.getInt("week_day_id");
        weekDay.name = rs.getString("week_day_name");
        return weekDay;
    }

    static boolean createTable() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `week_days` (" +
                    " `week_day_id` INT NOT NULL," +
                    " `week_day_name` TEXT NOT NULL," +
                    " PRIMARY KEY (`week_day_id`)," +
                    " INDEX (`week_day_id`)," +
                    " UNIQUE (`week_day_id`)" +
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
