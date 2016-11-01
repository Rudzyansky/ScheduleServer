package ru.falseteam.schedule.server.console.commands;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.console.CommandAbstract;

import java.util.Date;

public class Uptime extends CommandAbstract {
    private static long uptime = System.currentTimeMillis();


    public Uptime() {
        super("uptime");
    }

    @Override
    public void exec(String params) {
        // Выводим uptime;
        long uptime = System.currentTimeMillis() - Uptime.uptime;
        long tmp = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Uptime: ");

        tmp = uptime / 1000 / 60 / 60 / 24;
        if (tmp != 0)
            sb.append(tmp).append("day "); //TODO пофиксить тут.
        tmp = uptime / 1000 / 60 / 60 % 24;
        sb.append(tmp).append("h");
        tmp = uptime / 1000 / 60 % 60;
        if (tmp < 10) sb.append('0');
        sb.append(tmp).append("m");
        tmp = uptime / 1000 % 60;
        if (tmp < 10) sb.append('0');
        sb.append(tmp);
        sb.append("s\n");

        //Выводим данные об использовании памяти.
        Runtime runtime = Runtime.getRuntime();
        long totalMemoryMB = runtime.totalMemory() / 1024 / 1024;
        long usedMemoryMB = totalMemoryMB - runtime.freeMemory() / 1024 / 1024;
        int percent = (int) (usedMemoryMB / ((double) totalMemoryMB) * 100);

        sb.append("Memory used: ").append(usedMemoryMB).append("MB / ")
                .append(totalMemoryMB).append("MB (").append(percent).append("%)");

        Console.print(sb.toString());
    }
}
