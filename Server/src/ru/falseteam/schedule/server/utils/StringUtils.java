package ru.falseteam.schedule.server.utils;

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
        long tmp;
        StringBuilder sb = new StringBuilder();
        tmp = uptime / 1000 / 60 / 60 / 24;
        if (tmp != 0) sb.append(tmp).append("days "); //TODO пофиксить тут.
        tmp = uptime / 1000 / 60 / 60 % 24;
        if (tmp < 10) sb.append('0');
        sb.append(tmp).append(':');
        tmp = uptime / 1000 / 60 % 60;
        if (tmp < 10) sb.append('0');
        sb.append(tmp).append(':');
        tmp = uptime / 1000 % 60;
        if (tmp < 10) sb.append('0');
        sb.append(tmp);
        sb.append('.');
        sb.append(uptime % 1000);
        return sb.toString();
    }
}
