package ru.falseteam.schedule.server;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.server.console.commands.Online;
import ru.falseteam.schedule.server.console.commands.SetGroup;
import ru.falseteam.schedule.server.console.commands.Test;
import ru.falseteam.schedule.server.console.commands.Uptime;
import ru.falseteam.schedule.server.socket.Worker;
import ru.falseteam.vframe.VFrame;
import ru.falseteam.vframe.config.ConfigLoader;
import ru.falseteam.vframe.config.LoadFromConfig;
import ru.falseteam.vframe.console.ConsoleWorker;
import ru.falseteam.vframe.console.DefaultStopCommand;
import ru.falseteam.vframe.sql.SQLConnection;

/**
 * Основная точка входа.
 *
 * @author Vladislav Sumin
 * @author Evgeny Rudzyansky
 * @version 2.0
 */
public class Main {
    @LoadFromConfig(defaultValue = "")
    private static String version;

    private static Logger log = LogManager.getLogger();
    public static VkApiClient vk;

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        ConfigLoader.load(Main.class);
        log.info("Server version {} has been started", version);

        VFrame.init();
        // Инициализация клиента вк.
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        // Инициализация служебных модулей.
        ConsoleWorker.addCommand(new Online());
        ConsoleWorker.addCommand(new SetGroup());
        ConsoleWorker.addCommand(new Uptime());
        ConsoleWorker.addCommand(new Test());
        ConsoleWorker.addCommand(new DefaultStopCommand(Main::stop));
        ConsoleWorker.startListenAsDaemon();

        // Инициализация основных модулей.
        SQLConnection.init();
        Worker.init(); // Сервер сокет
        ru.falseteam.schedule.server.updater.Worker.init();
    }

    private static void stop() {
        log.info("Server stopping...");

        // Остановка ссновных модулей.
        ru.falseteam.schedule.server.updater.Worker.stop();
        Worker.stop(); // Сервер сокет
        SQLConnection.stop();

        // Остановка служебных модулей.
        VFrame.stop();
    }
}
