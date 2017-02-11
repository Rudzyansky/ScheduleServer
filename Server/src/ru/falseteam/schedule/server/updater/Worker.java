package ru.falseteam.schedule.server.updater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.vframe.VFrame;
import ru.falseteam.vframe.config.ConfigLoader;
import ru.falseteam.vframe.config.LoadFromConfig;
import ru.falseteam.vframe.socket.VFKeystore;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.IOException;

public class Worker implements Runnable {

    @LoadFromConfig(defaultValue = "7102")
    private static int updatePort;
    @LoadFromConfig(filename = "keystore", defaultValue = "")
    private static String keystorePublicPassword;
    @LoadFromConfig(filename = "keystore", defaultValue = "")
    private static String keystorePrivatePassword;
    @LoadFromConfig(filename = "keystore", defaultValue = "")
    private static String keystorePath;

    private static Logger log = LogManager.getLogger(Worker.class.getName());
    private static SSLServerSocket ss;

    public static void init() {
        ConfigLoader.load(Worker.class);
        new Thread(new Worker(), "UpdaterWorker").start();
    }

    public static void stop() {
        try {
            ss.close();
            log.trace("Port {} closed", updatePort);
        } catch (Exception e) {
            log.error("Can not close server socket", e);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            VFKeystore k = new VFKeystore(keystorePath, keystorePublicPassword, keystorePrivatePassword);
            ss = (SSLServerSocket) k.getSSLContext().getServerSocketFactory().createServerSocket(updatePort);
            VFrame.print("[Updater] port " + ss.getLocalPort() + " open and listening");
            if (ss == null) return;
            while (true) new Connection((SSLSocket) ss.accept());
        } catch (IOException ignore) {
        }
    }
}
