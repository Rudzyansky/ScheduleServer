package ru.falseteam.schedule.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
    public static final String DEFAULT_MARGIN = "           ";
    public static final int DEFAULT_MARGIN_LENGTH = DEFAULT_MARGIN.length();
    public static final int SHORT_DEFAULT_MARGIN_LENGTH = 4;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    public static void err(String str) {
        System.out.println(new SimpleDateFormat("[HH:mm:ss.S] ").format(new Date()) + ANSI_RED + str + ANSI_RESET);
    }
}
