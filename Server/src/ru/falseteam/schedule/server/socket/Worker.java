package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.Schedule;
import ru.falseteam.schedule.server.StaticSettings;
import ru.falseteam.schedule.server.socket.commands.Ping;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Worker implements Runnable {

    private static ServerSocket ss;
    private static final List<Connection> clients = new LinkedList<>();
    private static int connectionsFromAllTime = 0;

    private static TimerTask ping = new TimerTask() {
        @Override
        public void run() {
            Map<String, Object> request = Ping.getRequest();
            synchronized (clients) {
                Iterator<Connection> iterator = clients.iterator();
                while (iterator.hasNext()) {
                    Connection c = iterator.next();
                    if (System.currentTimeMillis() - c.getLastPing() > 95 * 1000) {
                        iterator.remove();
                        c.disconnect();
                    } else {
                        c.send(request);
                    }
                }
            }
        }
    };

    public static void init() {
        new Thread(new Worker()).start();
        Schedule.addPeriodicalTask(ping, 30 * 1000);
    }

    public static void stop() {
        try {
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Console.print("Port " + StaticSettings.getPort() + " closed");
    }

    static void removeFromList(Connection c) {
        clients.remove(c);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            ss = ssf.createServerSocket(StaticSettings.getPort());
            Console.print("Port " + StaticSettings.getPort() + " has been binded");
            while (true) {
                clients.add(new Connection(ss.accept()));
                ++connectionsFromAllTime;
            }
        } catch (IOException ignore) {
            // Normal situations throw while server socket closing.
        }
    }

    public static List<Connection> getClients() {
        return clients;
    }

    public static int getConnectionsFromAllTime() {
        return connectionsFromAllTime;
    }
}
