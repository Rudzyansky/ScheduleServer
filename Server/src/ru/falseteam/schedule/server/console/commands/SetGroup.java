package ru.falseteam.schedule.server.console.commands;

import org.apache.commons.cli.CommandLine;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.console.CommandAbstract;

public class SetGroup extends CommandAbstract {
    public SetGroup() {
        //noinspection SpellCheckingInspection
        super("setgroup");

        addOptions("g", "group", 1, true, "new group from user");
        addOptions("i", "id", 1, false, "user vk id");
        addOptions("n", "name", 2, false, "user family and name");
    }

    @Override
    public void exec(CommandLine commandLine) {
        Groups group;
        try {
            group = Groups.valueOf(commandLine.getOptionValue("group"));
        } catch (IllegalArgumentException e) {
            Console.err("Group " + commandLine.getOptionValue("group") + " not found");
            return;
        }

        String id = commandLine.getOptionValue("id");
        String name = commandLine.getOptionValue("name");
        if (id == null && name == null) {
            Console.err("Missing one of required options: id, name");
            return;
        }

        if (id != null) {

        } else {

        }

    }
}
