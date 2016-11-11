package ru.falseteam.schedule.server.sql;

import java.sql.SQLException;

import static ru.falseteam.schedule.server.sql.SQLConnection.executeUpdate;

/**
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class WeekNumberInfo {
    private static boolean createTableWeekNumbers() {
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
                    "(1, '1 февраля', 10)," +
                    "(2, '1 сентября', 40);");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
