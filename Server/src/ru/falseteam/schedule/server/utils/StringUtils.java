package ru.falseteam.schedule.server.utils;

import ru.falseteam.schedule.server.console.commands.Uptime;

public class StringUtils {
    public static String addMargin(String data, int margin) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < margin; ++i) {
            sb.append(' ');
        }
        sb.append(data.replaceAll("\n", "\n" + sb.toString()));
        return sb.toString();
    }

    public static String getUptime(long uptime) {
        uptime = System.currentTimeMillis() - uptime;
        long tmp = 0;
        StringBuilder sb = new StringBuilder();
        tmp = uptime / 1000 / 60 / 60 / 24;
        if (tmp != 0)
            sb.append(tmp).append("day "); //TODO пофиксить тут.
        tmp = uptime / 1000 / 60 / 60 % 24;
        sb.append(tmp).append('h');
        tmp = uptime / 1000 / 60 % 60;
        if (tmp < 10) sb.append('0');
        sb.append(tmp).append('m');
        tmp = uptime / 1000 % 60;
        if (tmp < 10) sb.append('0');
        sb.append(tmp);
        sb.append('s');
        return sb.toString();
    }
}
