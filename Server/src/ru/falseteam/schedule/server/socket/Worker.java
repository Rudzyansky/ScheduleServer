package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.Schedule;
import ru.falseteam.schedule.server.StaticSettings;
import ru.falseteam.schedule.server.socket.commands.Ping;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.*;

public class Worker implements Runnable {

    private static SSLServerSocket ss;
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

    private void initSSL() {
        try {
            String password = "public_pass";
            String passwordSigned = "private_pass";
            String algorithm = KeyManagerFactory.getDefaultAlgorithm();

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(StaticSettings.CONFIG_FOLDER + "/keystore.jks"), password.toCharArray());
            Console.print("KeyStore has been loaded");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, passwordSigned.toCharArray());
            Console.print("KeyManagerFactory has been initialized");

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(ks);
            Console.print("TrustManagerFactory has been initialized");

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);
            Console.print("SSLContext has been initialized");

            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            ss = (SSLServerSocket) ssf.createServerSocket(StaticSettings.getPort());
            Console.print("Port " + StaticSettings.getPort() + " has been binded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            initSSL();
            while (true) {
                clients.add(new Connection((SSLSocket) ss.accept()));
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
