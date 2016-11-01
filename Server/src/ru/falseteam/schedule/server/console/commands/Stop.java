package ru.falseteam.schedule.server.console.commands;

import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.console.CommandAbstract;

public class Stop extends CommandAbstract {
    public Stop() {
        super("stop");
    }

    @Override
    public void exec(String params) {
        Main.stop();
    }
}
