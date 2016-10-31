package ru.falseteam.schedule.server;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
            if (!main.exists() && main.createNewFile())
                createMainConfigFile(main);

            if (!database.exists() && database.createNewFile())
                createDatabaseConfigFile(database);
        } catch (IOException e) {
            Console.err("Can not create config file: " + CONFIG_FOLDER);
            Main.stop();
        }

        // Load config
        try {
            Properties properties = new Properties();

            properties.loadFromXML(new FileInputStream(main));
            port = Integer.parseInt(properties.getProperty("port"));
            updatePort = Integer.parseInt(properties.getProperty("update_port"));

            properties.loadFromXML(new FileInputStream(database));
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");

            if (user.equals("user") && password.equals("password"))
                Console.err("WARNING uses default database config, please edit "
                        + CONFIG_FOLDER + File.separator + CONFIG_DATABASE + " file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Console.print("Config loaded");
    }

    private static void createDatabaseConfigFile(File file) {
        Console.print("Initialize config " + CONFIG_DATABASE);
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://localhost:3306/schedule");
        properties.setProperty("user", "user");
        properties.setProperty("password", "password");
        try {
            properties.storeToXML(new FileOutputStream(file), "");
        } catch (IOException e) {
            Console.err("Can not open file stream from " + file.getPath());
        }
    }

    private static void createMainConfigFile(File file) {
        Console.print("Initialize config " + CONFIG_MAIN);
        Properties properties = new Properties();
        properties.setProperty("port", "7101");
        properties.setProperty("update_port", "7102");
        try {
            properties.storeToXML(new FileOutputStream(file), "");
        } catch (IOException e) {
            Console.err("Can not open file stream from " + file.getPath());
        }
    }

    public static int getPort() {
        return port;
    }

    public static int getUpdatePort() {
        return updatePort;
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
