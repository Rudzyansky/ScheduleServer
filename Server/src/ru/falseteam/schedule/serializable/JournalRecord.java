package ru.falseteam.schedule.serializable;

import java.io.Serializable;

/**
 * Structure contains all information about JournalRecord.
 *
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class JournalRecord implements Serializable {
    public int id;
    public java.sql.Date date;
    public WeekDay weekDay;
    public LessonNumber lessonNumber;
    public Lesson lesson;
    public boolean[] was;

    // Empty private constructor.
    private JournalRecord() {
    }

    public static class Factory {
        public static JournalRecord getDefault() {
            JournalRecord record = new JournalRecord();
            record.date = new java.sql.Date(new java.util.Date().getTime());
            record.weekDay = new WeekDay();
            record.lessonNumber = new LessonNumber();
            record.lesson = Lesson.Factory.getDefault();
            record.was = new boolean[30];
            return record;
        }
    }
}
