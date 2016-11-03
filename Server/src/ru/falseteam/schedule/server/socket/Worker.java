package ru.falseteam.schedule.server.socket;

import com.sun.istack.internal.NotNull;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.StaticSettings;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

public class Worker implements Runnable {

    private static ServerSocket ss;
    private static List<Connection> clients = new LinkedList<>();
    private static int connectionsFromAllTime = 0;

    public static void init() {
        new Thread(new Worker()).start();
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
