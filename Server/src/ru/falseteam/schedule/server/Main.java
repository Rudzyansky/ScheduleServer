package ru.falseteam.schedule.server;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import ru.falseteam.schedule.server.console.ConsoleWorker;
import ru.falseteam.schedule.server.socket.Worker;
import ru.falseteam.schedule.server.sql.SQLConnection;

public class Main {
    public static VkApiClient vk;

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        Console.print("Server has been started");

        // Инициализация клиента вк.
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        ConsoleWorker.init();
        StaticSettings.init();
        Schedule.init();
        SQLConnection.init();
        Worker.init(); // Сервер сокет
        ru.falseteam.schedule.server.updater.Worker.init();
    }

    public static void stop() {
        Console.print("Server stopping...");
        Schedule.stop();
        ru.falseteam.schedule.server.updater.Worker.stop();
        Worker.stop(); // Сервер сокет
        SQLConnection.stop();
        ConsoleWorker.stop();
    }

}
