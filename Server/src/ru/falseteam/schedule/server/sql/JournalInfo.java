package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.server.socket.Worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.falseteam.vframe.sql.SQLConnection.*;

/**
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class JournalInfo {
    private static final String table = "journal";

    static {
        Worker.getS().getSubscriptionManager().addEvent(
                "GetJournal", JournalInfo::getJournalForSubscriptions,
                Groups.developer, Groups.admin, Groups.user);
    }

    private static Map<String, Object> getJournalForSubscriptions() {
        Map<String, Object> map = new HashMap<>();
        map.put("journal", getJournal());
        return map;
    }

    private static void onDataUpdate() {
        Worker.getS().getSubscriptionManager().onEventDataChange("GetJournal", getJournalForSubscriptions());
    }

    /**
     * getTemplates load table to variable
     *
     * @return {@link List<JournalRecord>}, null if SQLException
     */
    public static List<JournalRecord> getJournal() {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `journal`" +
                    " NATURAL JOIN (`week_days`, `lesson_numbers`, `lessons`)" +
                    " ORDER BY `date`, `lesson_number_id`;");
            List<JournalRecord> records = new ArrayList<>();
            if (!rs.first()) return records;
            do records.add(getRecord(rs));
            while (rs.next());
            return records;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<JournalRecord> getDay(final java.sql.Date date) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `journal`" +
                    " NATURAL JOIN (`week_days`, `lesson_numbers`, `lessons`)" +
                    " WHERE `date` LIKE '" + date + "'" +
                    " ORDER BY `lesson_number_id`;");
            List<JournalRecord> records = new ArrayList<>();
            if (!rs.first()) return records;
            do records.add(getRecord(rs));
            while (rs.next());
            return records;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * getRecord load record from table `journal` using `date` field
     *
     * @param date - date
     * @return {@link JournalRecord} if exists in base, {null} if not exists or SQLException
     */
    public static JournalRecord getRecord(final java.sql.Date date, final int lessonNumber) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `journal`" +
                    " NATURAL JOIN (`week_days`, `lesson_numbers`, `lessons`)" +
                    " WHERE `date` LIKE '" + date + "'" +
                    " AND `lesson_number_id` LIKE '" + lessonNumber + "';");
            return rs.first() ? getRecord(rs) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * getRecord load record from table `journal` using `date` field
     *
     * @param id - id in base
     * @return {@link JournalRecord} if exists in base, {null} if not exists or SQLException
     */
    public static JournalRecord getRecord(final int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM `journal`" +
                    " NATURAL JOIN (`week_days`, `lesson_numbers`, `lessons`)" +
                    " WHERE `id` LIKE '" + id + "';");
            return rs.first() ? getRecord(rs) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param rs - {@link ResultSet}, из которого вытаскиваем инфу
     * @return {@link JournalRecord} if exists in base, {null} if not exists SQLException
     */
    private static JournalRecord getRecord(final ResultSet rs) throws SQLException {
        JournalRecord record = JournalRecord.Factory.getDefault();
        record.id = rs.getInt("id");
        record.date = rs.getDate("date");
        record.weekDay = WeekDayInfo.getWeekDay(rs);
        record.lessonNumber = LessonNumberInfo.getLessonNumber(rs);
        record.lesson = LessonInfo.getLesson(rs);
        byte[] rsb = rs.getBytes("presented");
        if (rsb != null) record.presented = BitSet.valueOf(rsb);

//        String was = rs.getString("presented");
//        if (was != null) {
//            byte[] bytes = Base64.getDecoder().decode(was);
//            ByteBuffer bb = ByteBuffer.allocate(bytes.length).put(bytes);
//            record.was = bb.asIntBuffer().array();
//        }

        record.task = rs.getString("task");
        return record;
    }

    public static boolean updatePresented(final JournalRecord record) {
        try {
            String condition = "WHERE `id` LIKE '" + record.id + "'";
            PreparedStatement ps = update(table, condition, "presented");
            ps.setBytes(1, record.presented.toByteArray());
            ps.execute();
//            ByteBuffer bb = ByteBuffer.wrap(record.presented);
//            executeUpdate("UPDATE `templates` SET" +
//                    " `presented` = '" + bb + "'" +
//                    " WHERE `id` LIKE '" + record.id + "';");
            onDataUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addRecord(final JournalRecord record) {
        try {
            executeUpdate("INSERT INTO `journal` (`date`, `week_day_id`, `lesson_number_id`, `lesson_id`, `task`)" +
                    " VALUES ('" +
                    record.date + "', '" +
                    record.weekDay.id + "', '" +
                    record.lessonNumber.id + "', '" +
                    record.lesson.id + "', '" +
                    record.lesson.lastTask + "');");
            onDataUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TimerTask addRec = new TimerTask() {
        @Override
        public void run() {
            try {
                ResultSet rs = executeQuery("SELECT * FROM `journal`" +
                        " WHERE `date` LIKE '" + new java.sql.Date(new Date().getTime()) + "';");
                if (rs.first()) return;
                Calendar c = Calendar.getInstance();
                int dayOfWeek = (c.get(Calendar.DAY_OF_WEEK) - 1);
                if (dayOfWeek == 0) dayOfWeek = 7;
                int week = (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 5);
                int finalDayOfWeek = dayOfWeek;
                TemplateInfo.getTemplates().stream()
                        .filter(t -> (
                                t.weekDay.id == finalDayOfWeek && (
                                        (t.weeks.get(31) &&
                                                (t.weeks.get(30) || (t.weeks.get(29) && week % 2 == 1) || (!t.weeks.get(29) && week % 2 == 0))
                                        ) || t.weeks.get(week - 1)
                                )
                        ))
                        .forEach(t -> addRecord(JournalRecord.Factory.getFromTemplate(t)))
                ;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    public static boolean createTable() {
        try {
            //noinspection SpellCheckingInspection
            executeUpdate("CREATE TABLE `journal` (" +
                    " `id` INT NOT NULL AUTO_INCREMENT," +
                    " `date` DATE NOT NULL," +
                    " `week_day_id` INT NOT NULL," +
                    " `lesson_number_id` INT NOT NULL," +
                    " `lesson_id` INT NOT NULL," +
                    " `task` TEXT," +
                    " `presented` BINARY(4)," +

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
