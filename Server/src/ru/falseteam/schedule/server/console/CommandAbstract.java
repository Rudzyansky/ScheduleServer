package ru.falseteam.schedule.server.console;

import org.apache.commons.cli.*;
import ru.falseteam.schedule.server.Console;

public abstract class CommandAbstract implements CommandInterface {
    private final String name;

    private Options options = new Options();

    public CommandAbstract(String name) {
        this.name = name;
    }

    protected void addOptions(String shortOptions, String longOptions,
                              int argsCount, boolean required, String descriptions) {
        boolean hasArgs = argsCount != 0;
        Option option = new Option(shortOptions, longOptions, hasArgs, descriptions);
        option.setArgs(argsCount);
        option.setRequired(required);
        option.setArgName(longOptions);
        options.addOption(option);
    }

    @Override
    public void exec(String params) {
        try {
            CommandLine commandLine = new DefaultParser().parse(options, params.split(" "));
            exec(commandLine);
        } catch (ParseException e) {
            Console.err(e.getMessage());
        }
    }

    @Override
    public final String getName() {
        return name;
    }
}
