package ru.falseteam.schedule.server.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.server.Schedule;
import ru.falseteam.schedule.server.StaticSettings;
import ru.falseteam.schedule.server.socket.commands.Ping;
import ru.falseteam.schedule.server.utils.SSLServerSocketFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.*;

/**
 * Управляет сервернум сокетом, создает соединения с пользователями.
 *
 * @author Vladislav Sumin
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class Worker implements Runnable {
    private static Logger log = LogManager.getLogger(Worker.class.getName());

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
        new Thread(new Worker(), "ServerSocketWorker").start();
        Schedule.addPeriodicalTask(ping, 30 * 1000);
    }

    public static void stop() {
        try {
            ss.close();
            log.trace("Port {} closed", StaticSettings.getPort());
        } catch (Exception e) {
            log.error("Can not close server socket", e);
        }

        synchronized (clients) {
            Iterator<Connection> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Connection connection = iterator.next();
                iterator.remove();
                connection.disconnect();
            }
        }
    }

    static void removeFromList(Connection c) {
        clients.remove(c);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        ss = SSLServerSocketFactory.createServerSocket(StaticSettings.getPort());
        if (ss == null) return;
        try {
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
