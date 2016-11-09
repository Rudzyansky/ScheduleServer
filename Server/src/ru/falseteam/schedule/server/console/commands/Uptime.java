package ru.falseteam.schedule.server.console.commands;

import org.apache.commons.cli.CommandLine;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.console.CommandAbstract;
import ru.falseteam.schedule.server.utils.StringUtils;

public class Uptime extends CommandAbstract {
    private static long uptime = System.currentTimeMillis();


    public Uptime() {
        super("uptime");
    }

    @Override
    public void exec(CommandLine commandLine) {
        // Выводим uptime;
        StringBuilder sb = new StringBuilder();
        sb.append("Uptime: ").append(StringUtils.getUptime(uptime)).append('\n');

        //Выводим данные об использовании памяти.
        sb.append(Console.DEFAULT_MARGIN);
        Runtime runtime = Runtime.getRuntime();
        long totalMemoryMB = runtime.totalMemory() / 1024 / 1024;
        long usedMemoryMB = totalMemoryMB - runtime.freeMemory() / 1024 / 1024;
        int percent = (int) (usedMemoryMB / ((double) totalMemoryMB) * 100);

        sb.append("Memory used: ").append(usedMemoryMB).append("MB / ")
                .append(totalMemoryMB).append("MB (").append(percent).append("%)");

        Console.print(sb.toString());
    }
}
