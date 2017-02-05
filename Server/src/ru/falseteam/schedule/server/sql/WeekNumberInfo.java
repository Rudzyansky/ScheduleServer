package ru.falseteam.schedule.server.sql;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

/**
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
class WeekNumberInfo {
    static boolean createTable() {
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
            int year = LocalDateTime.now().getYear();
            executeUpdate("INSERT INTO `week_numbers`" +
                    " (`week_number_id`, `week_number_name`, `week_number`) VALUES" +
                    "(1, '6 февраля', " + new Calendar.Builder().setDate(year, 2, 6).build().get(Calendar.WEEK_OF_YEAR) + ")," +
                    "(2, '1 сентября', " + new Calendar.Builder().setDate(year, 9, 1).build().get(Calendar.WEEK_OF_YEAR) + ");");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
