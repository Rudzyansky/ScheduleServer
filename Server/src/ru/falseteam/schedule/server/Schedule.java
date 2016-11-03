package ru.falseteam.schedule.server;

import java.util.Timer;
import java.util.TimerTask;

public class Schedule {
    private static Timer timer;

    static void init() {
        timer = new Timer();
    }

    static void stop() {
        timer.cancel();
    }

    public static void addPeriodicalTask(TimerTask tt, long period) {
        timer.schedule(tt, 0, period);
    }
}
