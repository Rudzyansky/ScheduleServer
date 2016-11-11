package ru.falseteam.schedule.server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Содержит все статические поля, загружаемые из конфигов.
 *
 * @author Sumin Vladislav
 * @version 1.0
 */
public class StaticSettings {
    static final String VERSION = "2.0b";
    public static final String CONFIG_FOLDER = "config";

    //Файлы конфигурации.
    private static final String CONFIG_MAIN = "main.xml";
    private static final String CONFIG_DATABASE = "database.xml";

    // Main
    private static int port;
    private static int updatePort;
    private static String publicPass;
    private static String privatePass;
    private static String lastClientVersion;
    private static String lastClientPath;

    // Database
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    private static Logger log = LogManager.getLogger(StaticSettings.class.getName());

    static void init() {
        log.trace("Loading settings from config files");
        final String separator = File.separator;

        File config = new File(CONFIG_FOLDER);
        if (!config.exists() && !config.mkdir()) {
            log.fatal("Can not create config folder. Server will be stop");
            Main.stop();
        }

        File main = new File(CONFIG_FOLDER + separator + CONFIG_MAIN);
        File database = new File(CONFIG_FOLDER + separator + CONFIG_DATABASE);

        try {
            if (!main.exists() && main.createNewFile()) {
                log.trace("Initialize config {}", CONFIG_MAIN);
                new Properties().storeToXML(new FileOutputStream(main), "");
            }
            loadMainConfigFile(main);

            if (!database.exists() && database.createNewFile()) {
                log.trace("Initialize config {}", CONFIG_DATABASE);
                new Properties().storeToXML(new FileOutputStream(database), "");
            }
            loadDatabaseConfigFile(database);

        } catch (IOException e) {
            log.fatal("Can not access to folder " + CONFIG_FOLDER, e);
            Main.stop();
        }
    }

    private static void loadDatabaseConfigFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(file));
        Set<String> names = properties.stringPropertyNames();
        boolean save = false;
        if (!names.contains("url")) {
            properties.setProperty("url", "jdbc:mysql://localhost:3306/schedule");
            save = true;
        }
        if (!names.contains("user")) {
            properties.setProperty("user", "user");
            save = true;
        }
        if (!names.contains("password")) {
            properties.setProperty("password", "password");
            save = true;
        }
        if (save)
            properties.storeToXML(new FileOutputStream(file), "");

        dbUrl = properties.getProperty("url");
        dbUser = properties.getProperty("user");
        dbPassword = properties.getProperty("password");

        if (dbUser.equals("user") && dbPassword.equals("password"))
            log.error("WARNING uses default database config, please edit {}{}{} file",
                    CONFIG_FOLDER, File.separator, CONFIG_DATABASE);
    }

    private static void loadMainConfigFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(file));
        Set<String> names = properties.stringPropertyNames();
        boolean save = false;

        if (!names.contains("port")) {
            properties.setProperty("port", "7101");
            save = true;
        }
        if (!names.contains("update_port")) {
            properties.setProperty("update_port", "7102");
            save = true;
        }
        if (!names.contains("publicPass")) {
            properties.setProperty("publicPass", "publicPass");
            save = true;
        }
        if (!names.contains("privatePass")) {
            properties.setProperty("privatePass", "privatePass");
            save = true;
        }
        if (!names.contains("lastClientVersion")) {
            properties.setProperty("lastClientVersion", "0");
            save = true;
        }
        if (!names.contains("lastClientPath")) {
            properties.setProperty("lastClientPath", "./schedule.apk");
            save = true;
        }
        if (save)
            properties.storeToXML(new FileOutputStream(file), "");

        port = Integer.parseInt(properties.getProperty("port"));
        updatePort = Integer.parseInt(properties.getProperty("update_port"));
        publicPass = properties.getProperty("publicPass");
        privatePass = properties.getProperty("privatePass");
        lastClientVersion = properties.getProperty("lastClientVersion");
        lastClientPath = properties.getProperty("lastClientPath");
    }

    public static int getPort() {
        return port;
    }

    public static int getUpdatePort() {
        return updatePort;
    }

    public static String getPublicPass() {
        return publicPass;
    }

    public static String getPrivatePass() {
        return privatePass;
    }

    public static String getLastClientVersion() {
        return lastClientVersion;
    }

    public static String getLastClientPath() {
        return lastClientPath;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static String getDbPassword() {
        return dbPassword;
    }
}
