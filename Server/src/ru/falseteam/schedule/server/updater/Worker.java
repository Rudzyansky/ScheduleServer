package ru.falseteam.schedule.server.updater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.StaticSettings;
import ru.falseteam.schedule.server.utils.SSLServerSocketFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.IOException;

public class Worker implements Runnable {
    private static Logger log = LogManager.getLogger(Worker.class.getName());

    private static SSLServerSocket ss;

    public static void init() {
        new Thread(new Worker(), "UpdaterWorker").start();
    }

    public static void stop() {
        try {
            ss.close();
            log.trace("Port {} closed", StaticSettings.getUpdatePort());
        } catch (Exception e) {
            log.error("Can not close server socket", e);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        ss = SSLServerSocketFactory.createServerSocket(StaticSettings.getUpdatePort());
        if (ss == null) return;
        try {
            while (true) new Connection((SSLSocket) ss.accept());
        } catch (IOException ignore) {
        }
    }
}
