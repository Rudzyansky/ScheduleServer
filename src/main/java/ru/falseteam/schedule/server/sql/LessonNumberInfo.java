package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.LessonNumber;
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
public class LessonNumberInfo {

    static {
        Worker.getS().getSubscriptionManager().addEvent("GetLessonNumbers",
                LessonNumberInfo::getLessonNumbersForSubscriptions,
                Groups.developer, Groups.admin, Groups.user);
    }

    private static Map<String, Object> getLessonNumbersForSubscriptions() {
        Map<String, Object> map = new HashMap<>();
        map.put("lesson_numbers", getLessonNumbers());
        return map;
    }

    public static List<LessonNumber> getLessonNumbers() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `lesson_numbers`;");
            List<LessonNumber> lessonNumbers = new ArrayList<>();
            if (!rs.first()) return lessonNumbers;
            do lessonNumbers.add(getLessonNumber(rs));
            while (rs.next());
            return lessonNumbers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static LessonNumber getLessonNumber(ResultSet rs) throws SQLException {
        LessonNumber lessonNumber = new LessonNumber();
        lessonNumber.id = rs.getInt("lesson_number_id");
        lessonNumber.begin = rs.getTime("lesson_number_begin");
        lessonNumber.end = rs.getTime("lesson_number_end");
        return lessonNumber;
    }

    public static boolean createTable() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `lesson_numbers` (" +
                    " `lesson_number_id` INT NOT NULL," +
                    " `lesson_number_begin` TIME NOT NULL," +
                    " `lesson_number_end` TIME NOT NULL," +
                    " PRIMARY KEY (`lesson_number_id`)," +
                    " INDEX (`lesson_number_id`)," +
                    " UNIQUE (`lesson_number_id`)" +
                    ") ENGINE=InnoDB;");
            executeUpdate("INSERT INTO `lesson_numbers` (`lesson_number_id`," +
                    " `lesson_number_begin`, `lesson_number_end`) VALUES" +
                    "(1, '09:00:00', '10:30:00')," +
                    "(2, '10:40:00', '12:10:00')," +
                    "(3, '13:00:00', '14:30:00')," +
                    "(4, '14:40:00', '16:10:00')," +
                    "(5, '16:20:00', '17:50:00')," +
                    "(6, '18:00:00', '19:30:00');");
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }
}
