package ru.falseteam.schedule.server.console;

import ru.falseteam.schedule.server.Main;

class _Stop extends CommandAbstract {
    _Stop() {
        super("stop");
    }

    @Override
    public void exec(String params) {
        Main.stop();
    }
}
