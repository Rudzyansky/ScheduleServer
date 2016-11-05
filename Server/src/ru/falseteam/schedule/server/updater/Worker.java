package ru.falseteam.schedule.server.updater;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.StaticSettings;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

public class Worker implements Runnable {

    private static SSLServerSocket ss;

    public static void init() {
        new Thread(new Worker()).start();
    }

    public static void stop() {
        try {
            ss.close();
        } catch (Exception ignore) {
        }
        Console.print("Port " + StaticSettings.getUpdatePort() + " closed");
    }

    private void initSSL() {
        try {
            String algorithm = KeyManagerFactory.getDefaultAlgorithm();

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(StaticSettings.CONFIG_FOLDER + "/keystore.jks"),
                    StaticSettings.getPublicPass().toCharArray());
            Console.print("[updater] KeyStore has been loaded");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, StaticSettings.getPrivatePass().toCharArray());
            Console.print("[updater] KeyManagerFactory has been initialized");

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(ks);
            Console.print("[updater] TrustManagerFactory has been initialized");

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);
            Console.print("[updater] SSLContext has been initialized");

            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            ss = (SSLServerSocket) ssf.createServerSocket(StaticSettings.getUpdatePort());
            Console.print("[updater] Port " + StaticSettings.getUpdatePort() + " has been binded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            initSSL();
            while (true) new Connection((SSLSocket) ss.accept());
        } catch (IOException ignore) {
        }
    }
}
