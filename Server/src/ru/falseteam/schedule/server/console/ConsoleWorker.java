package ru.falseteam.schedule.server.console;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.console.commands.Connections;
import ru.falseteam.schedule.server.console.commands.Stop;
import ru.falseteam.schedule.server.console.commands.Uptime;

import java.io.IOException;
import java.util.*;

/**
 * @author Vladislav Sumin
 */
public class ConsoleWorker implements Runnable {
    private static Scanner scanner;
    private static Map<String, CommandInterface> commands = new HashMap<>();

    static {
        addNewCommand(new Stop());
        addNewCommand(new Uptime());
        addNewCommand(new Connections());
    }

    private static void addNewCommand(CommandInterface command) {
        commands.put(command.getName(), command);
    }

    public static void init() {
        new Thread(new ConsoleWorker()).start();
    }

    public static void stop() {
        try {
            System.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        while (true) {
            try {
                String[] command = scanner.nextLine().split(" ", 2);
                if (commands.containsKey(command[0]))
                    if (command.length > 1)
                        commands.get(command[0]).exec(command[1]);
                    else
                        commands.get(command[0]).exec(null);
                else
                    Console.err("Command not found");
            } catch (Exception ignore) {
                // normal situation. If System.in close Scanner throw exception
                return;
            }
        }
    }
}
