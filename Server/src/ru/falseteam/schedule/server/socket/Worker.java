package ru.falseteam.schedule.server.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.vframe.VFrameRuntimeException;
import ru.falseteam.vframe.config.ConfigLoader;
import ru.falseteam.vframe.config.LoadFromConfig;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.SocketWorker;
import ru.falseteam.vframe.socket.VFKeystore;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Управляет сервернум сокетом, создает соединения с пользователями.
 *
 * @author Vladislav Sumin
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class Worker {
    private static final Logger log = LogManager.getLogger();

    @LoadFromConfig(filename = "keystore", defaultValue = "publicPassword")
    private static String keystorePublicPassword;
    @LoadFromConfig(filename = "keystore", defaultValue = "privatePassword")
    private static String keystorePrivatePassword;
    @LoadFromConfig(filename = "keystore", defaultValue = "config/keystore.jks")
    private static String keystorePath;
    @LoadFromConfig(defaultValue = "7101")
    private static int port;

    private static SocketWorker ssw;

    public static void init() {
        ConfigLoader.load(Worker.class);
        VFKeystore keystore;
        try {
            keystore = new VFKeystore(keystorePath, keystorePublicPassword, keystorePrivatePassword);
        } catch (FileNotFoundException e) {
            log.fatal("VFrame: keystore file not found");
            throw new VFrameRuntimeException(e);
        }
        ssw = new SocketWorker(Connection::new, keystore, port);
    }

    public static List<ConnectionAbstract> getClients() {
        return ssw.getConnections();
    }

    public static void stop() {
        if (ssw != null) ssw.stop();
        ssw = null;
    }
}
