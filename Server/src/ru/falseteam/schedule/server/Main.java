package ru.falseteam.schedule.server;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import ru.falseteam.schedule.server.console.ConsoleWorker;
import ru.falseteam.schedule.server.socket.Worker;
import ru.falseteam.schedule.server.sql.SQLConnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class Main {
    public static VkApiClient vk;

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        int begin = new Calendar.Builder().setDate(now.getYear(), 9, 1).build().get(Calendar.WEEK_OF_YEAR);
        Calendar cal = new Calendar.Builder().setDate(now.getYear(), now.getMonthValue(), now.getDayOfMonth()).build();
        int current = cal.get(Calendar.WEEK_OF_YEAR);
        System.out.println(begin);
        System.out.println(current);
        System.out.println(current - begin);
//        start();
    }

    private static void start() {
        Console.print("Server has been started");
        Console.print("Server version " + StaticSettings.VERSION);
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
