package ru.falseteam.schedule.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    public static void print(String str) {
        System.out.println(timeTag() + str);
    }

    public static void err(String str) {

        System.out.println(timeTag() + ANSI_RED + str + ANSI_RESET);
    }

    private static String timeTag() {
        return new SimpleDateFormat("[HH:mm:ss] ").format(new Date());
    }
}
