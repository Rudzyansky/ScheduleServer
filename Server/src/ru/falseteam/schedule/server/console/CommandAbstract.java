package ru.falseteam.schedule.server.console;

public abstract class CommandAbstract implements CommandInterface {
    private final String name;

    public CommandAbstract(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }
}
