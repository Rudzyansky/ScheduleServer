package ru.falseteam.schedule.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс для выполнения переодических заданий в одном потоке.
 */
public class Schedule {
    private static Logger log = LogManager.getLogger(Schedule.class.getName());
    private static Timer timer;

    static void init() {
        timer = new Timer();
        log.trace("Schedule initialized.");
    }

    static void stop() {
        timer.cancel();
    }

    public static void addPeriodicalTask(TimerTask tt, long period) {
        log.trace("Adding periodical task to schedule.");
        timer.schedule(tt, 0, period);
    }
}
