package ru.falseteam.schedule.server;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.server.console.ConsoleWorker;
import ru.falseteam.schedule.server.socket.Worker;
import ru.falseteam.vframe.VFrame;
import ru.falseteam.vframe.sql.SQLConnection;

/**
 * Основная точка входа.
 *
 * @author Vladislav Sumin
 * @author Evgeny Rudzyansky
 * @version 2.0
 */
public class Main {
    private static Logger log = LogManager.getLogger();
    public static VkApiClient vk;

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        log.info("Server version {} has been started", StaticSettings.VERSION);

        VFrame.init();
        // Инициализация клиента вк.
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        // Инициализация служебных модулей.
        StaticSettings.init();
        ConsoleWorker.init();
        Schedule.init();

        // Инициализация основных модулей.
        SQLConnection.init();
        Worker.init(); // Сервер сокет
        ru.falseteam.schedule.server.updater.Worker.init();
    }

    public static void stop() {
        log.info("Server stopping...");

        // Остановка ссновных модулей.
        Schedule.stop();
        ru.falseteam.schedule.server.updater.Worker.stop();
        Worker.stop(); // Сервер сокет
        SQLConnection.stop();

        // Остановка служебных модулей.
        ConsoleWorker.stop();
        VFrame.stop();
    }

}
