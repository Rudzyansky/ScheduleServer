package ru.falseteam.schedule.server.updater;

import ru.falseteam.schedule.server.Console;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

public class Worker implements Runnable {

    private static ServerSocket ss;

    public static void init() {
        new Thread(new Worker()).start();
    }

    public static void stop() {
        try {
            ss.close();
        } catch (Exception ignore) {
        }
        Console.print("Port 7102 closed");
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            ss = ssf.createServerSocket(7102);
            Console.print("Port 7102 has been binded");
            while (true) new Connection(ss.accept());
        } catch (IOException ignore) {
        }
    }
}
