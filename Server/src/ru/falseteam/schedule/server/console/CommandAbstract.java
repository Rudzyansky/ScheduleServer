package ru.falseteam.schedule.server.console;

abstract class CommandAbstract implements CommandInterface {
    private final String name;

    public CommandAbstract(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }
}
