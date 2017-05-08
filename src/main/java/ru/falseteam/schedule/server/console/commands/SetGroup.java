package ru.falseteam.schedule.server.console.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.vframe.VFrame;
import ru.falseteam.vframe.console.CommandAbstract;

public class SetGroup extends CommandAbstract {
    public SetGroup() {
        addOption("g", "permissions", 1, true, "new permissions from user");
        addOption("i", "id", 1, false, "user vk id");
        addOption("n", "name", 2, false, "user family and name");
    }

    @Override
    public void exec(CommandLine commandLine) {
        //Проверяем группу.
        Groups group;
        try {
            group = Groups.valueOf(commandLine.getOptionValue("permissions"));
            if (group.equals(Groups.guest)) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            Console.err("Group " + commandLine.getOptionValue("permissions") + " not found");
            return;
        }

        //Проверяем id или имя.
        String id = commandLine.getOptionValue("id");
        String name = StringUtils.join(commandLine.getOptionValues("name"), ' ');
        if (id == null && name == null) {
            Console.err("Missing one of required options: id, name");
            return;
        }

        User user;
        if (id != null) {
            int vkId;
            try {
                vkId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                Console.err(id + " not integer");
                return;
            }
            user = UserInfo.getUser(vkId);
            if (user == null) {
                Console.err("User with id " + vkId + " not found");
                return;
            }
        } else {
            user = UserInfo.getUser(name);
            if (user == null) {
                Console.err("User with name " + name + " not found");
                return;
            }
        }
        user.permissions = group;
        UserInfo.updateUser(user);
        VFrame.print("Set permissions " + group.name() + " for user " + user.vkId + " " + user.name);
    }
}
