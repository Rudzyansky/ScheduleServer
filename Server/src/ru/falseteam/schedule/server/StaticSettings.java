package ru.falseteam.schedule.server;


import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * @author Sumin Vladislav
 */
public class StaticSettings {
    private static final String CONFIG_FOLDER = "config";

    private static final String CONFIG_MAIN = "main.xml";
    private static final String CONFIG_DATABASE = "database.xml";

    // Main
    private static int port;
    private static int updatePort;
    private static String lastClientVersion;
    private static String lastClientPath;

    // Database
    private static String url;
    private static String user;
    private static String password;

    static void init() {
        Console.print("Loading settings from config files");
        final String separator = File.separator;

        File config = new File(CONFIG_FOLDER);
        if (!config.exists() && !config.mkdir()) {
            Console.err("Can not create config folder.");
            Main.stop();
        }

        File main = new File(CONFIG_FOLDER + separator + CONFIG_MAIN);
        File database = new File(CONFIG_FOLDER + separator + CONFIG_DATABASE);
        try {
            if (!main.exists() && main.createNewFile()) {
                Console.print("Initialize config " + CONFIG_MAIN);
                new Properties().storeToXML(new FileOutputStream(main), "");
            }
            loadMainConfigFile(main);

            if (!database.exists() && database.createNewFile()) {
                Console.print("Initialize config " + CONFIG_DATABASE);
                new Properties().storeToXML(new FileOutputStream(database), "");
            }
            loadDatabaseConfigFile(database);

        } catch (IOException e) {
            Console.err("Can not create/write/read config file: " + CONFIG_FOLDER);
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

        url = properties.getProperty("url");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

        if (user.equals("user") && password.equals("password"))
            Console.err("WARNING uses default database config, please edit "
                    + CONFIG_FOLDER + File.separator + CONFIG_DATABASE + " file.");
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
        lastClientVersion = properties.getProperty("lastClientVersion");
        lastClientPath = properties.getProperty("lastClientPath");
    }

    public static int getPort() {
        return port;
    }

    public static int getUpdatePort() {
        return updatePort;
    }

    public static String getLastClientVersion() {
        return lastClientVersion;
    }

    public static String getLastClientPath() {
        return lastClientPath;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
