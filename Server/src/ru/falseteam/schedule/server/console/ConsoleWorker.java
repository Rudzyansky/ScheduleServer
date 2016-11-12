package ru.falseteam.schedule.server.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.console.commands.Online;
import ru.falseteam.schedule.server.console.commands.SetGroup;
import ru.falseteam.schedule.server.console.commands.Stop;
import ru.falseteam.schedule.server.console.commands.Uptime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Берет на себя работу с консольным вводом.
 * Обрабатывает команды пользователя.
 *
 * @author Vladislav Sumin
 * @version 1.0
 */
public class ConsoleWorker implements Runnable {
    private static Logger log = LogManager.getLogger(ConsoleWorker.class.getName());

    private static Map<String, CommandInterface> commands = new HashMap<>();

    static {
        addNewCommand(new Stop());
        addNewCommand(new Uptime());
        addNewCommand(new Online());
        addNewCommand(new SetGroup());
    }

    private static void addNewCommand(CommandInterface command) {
        commands.put(command.getName(), command);
    }

    public static void init() {
        new Thread(new ConsoleWorker(), "ConsoleWorker").start();
    }

    public static void stop() {
        try {
            System.in.close();
        } catch (IOException e) {
            log.error("System.in close error", e);
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        log.trace("ConsoleWorker initialized.");
        while (true) {
            try {
                //Отделяем команду от параметров.
                String[] command = scanner.nextLine().split(" ", 2);
                //Отсеиваем пустые команды.
                if (command[0].equals("")) continue;

                if (commands.containsKey(command[0]))
                    if (command.length > 1)
                        commands.get(command[0]).exec(command[1]);
                    else
                        commands.get(command[0]).exec("");
                else
                    Console.err("Command not found");
            } catch (NoSuchElementException ignore) {
                // normal situation. If System.in close Scanner throw exception
                return;
            }
        }
    }
}
