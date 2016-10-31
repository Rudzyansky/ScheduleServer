package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.server.Console;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Worker implements Runnable {

    private static ServerSocket ss;
    private static LinkedList<Connection> clients = new LinkedList<>();


    public static void init(){
        new Thread(new Worker()).start();
    }

    public static void stop() {
        try {
            ss.close();
        } catch (IOException ignore) {
        }
        Console.print("Port 7101 closed");
    }

    static void removeFromList(Connection c) {
        clients.remove(c);
    }

    @Override
    public void run() {
        try {
            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            ss = ssf.createServerSocket(7101);
            Console.print("Port 7101 has been binded");
            //noinspection InfiniteLoopStatement
            while (true) clients.add(new Connection(ss.accept()));
        } catch (IOException ignore) {
        }
    }
}
