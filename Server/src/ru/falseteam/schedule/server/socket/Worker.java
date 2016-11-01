package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.StaticSettings;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Worker implements Runnable {

    private static ServerSocket ss;
    private static LinkedList<Connection> clients = new LinkedList<>();

    private static int connectionsFromAllTime = 0;

    public static void init() {
        new Thread(new Worker()).start();
    }

    public static void stop() {
        try {
            ss.close();
        } catch (Exception ignore) {
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
        }
    }

    public static LinkedList<Connection> getClients() {
        return clients;
    }

    public static int getConnectionsFromAllTime() {
        return connectionsFromAllTime;
    }
}
