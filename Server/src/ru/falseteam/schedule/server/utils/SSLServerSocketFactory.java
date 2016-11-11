package ru.falseteam.schedule.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.server.Main;
import ru.falseteam.schedule.server.StaticSettings;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Создает фабрику сервеных сокетов использующих проверку подлинности по сертификату.
 *
 * @author Vladislav Sumin
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class SSLServerSocketFactory {
    private static Logger log = LogManager.getLogger(SSLServerSocketFactory.class.getName());

    private static javax.net.ssl.SSLServerSocketFactory ssf;

    private static boolean initSSL() {
        String algorithm = KeyManagerFactory.getDefaultAlgorithm();

        // Загружаем связку ключей.
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(StaticSettings.CONFIG_FOLDER + "/keystore.jks"),
                    StaticSettings.getPublicPass().toCharArray());
            log.trace("KeyStore has been loaded");
        } catch (CertificateException | NoSuchAlgorithmException | IOException | KeyStoreException e) {
            log.fatal("Can not load keyStore. Server will be stop.", e);
            Main.stop();
            return false;
        }

        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(keyStore, StaticSettings.getPrivatePass().toCharArray());
            //Console.print("KeyManagerFactory has been initialized");

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(keyStore);
            //Console.print("TrustManagerFactory has been initialized");

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);
            //Console.print("SSLContext has been initialized");

            ssf = sc.getServerSocketFactory();
            log.trace("ServerSocketFactory initialized");
        } catch (Exception e) {
            log.fatal("Can not create server socket. Server will be stop", e);
            Main.stop();
            return false;
        }
        return true;
    }

    /**
     * @param port port
     * @return {@link SSLServerSocket} if certificate valid,
     * {null} and stop server if certificate is invalid or not found.
     */
    public static synchronized SSLServerSocket createServerSocket(int port) {
        if (ssf == null) initSSL();
        if (ssf != null) try {
            SSLServerSocket socket = (SSLServerSocket) ssf.createServerSocket(port);
            log.trace("Port {} has bin binded", port);
            return socket;
        } catch (IOException e) {
            log.fatal("Cannot open port {}. Server will be stop.");
            Main.stop();
        }
        return null;

    }
}
