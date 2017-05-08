package ru.falseteam.schedule.server.sql;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;

import static ru.falseteam.vframe.sql.SQLConnection.executeUpdate;

/**
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class WeekNumberInfo {
    private static int first;
    private static int second;

    public static int getStartWeek() {
        int week = getCurrentWeekOfYear();
        if (week >= first && week < second) return first;
        else return second;
    }

    public static int getCurrentWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getCurrentWeekOfSemester() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (week >= first && week < second) week -= first - 1;
        else week -= second - 1;
        return week;
    }

    public static void init() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.YEAR, LocalDateTime.now().getYear());
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 6);
        first = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        second = calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean createTable() {
        init();
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `week_numbers` (" +
                    " `week_number_id` INT NOT NULL," +
                    " `week_number_name` TEXT NOT NULL," +
                    " `week_number` INT NOT NULL," +
                    " PRIMARY KEY (`week_number_id`)," +
                    " INDEX (`week_number_id`)," +
                    " UNIQUE (`week_number_id`)" +
                    ") ENGINE=InnoDB;");
            executeUpdate("INSERT INTO `week_numbers`" +
                    " (`week_number_id`, `week_number_name`, `week_number`) VALUES" +
                    "(1, '6 февраля', " + first + ")," +
                    "(2, '1 сентября', " + second + ");");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
