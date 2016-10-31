package ru.falseteam.schedule.server.socket;

public abstract class CommandAbstract implements CommandInterface {
    private final String name;

    public CommandAbstract(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
