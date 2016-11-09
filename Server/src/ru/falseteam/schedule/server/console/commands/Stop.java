package ru.falseteam.schedule.server.console.commands;

import org.apache.commons.cli.CommandLine;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.console.CommandAbstract;

public class Stop extends CommandAbstract {
    public Stop() {
        super("stop");
    }

    @Override
    public void exec(CommandLine commandLine) {
        Main.stop();
    }
}
