package ru.falseteam.schedule.server;


import com.sun.org.apache.xalan.internal.xslt.Process;

import java.io.File;
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

    public static void main(String[] args) {
        init();
    }

    static void init() {
        Console.print("Loading settings from config files");
        final String separator = File.separator;

        File config = new File(CONFIG_FOLDER);
        if (!config.exists() && !config.mkdir()) {
            Console.err("Can not create config folder.");
            Main.stop();
        }

        File main = new File(CONFIG_FOLDER + separator + CONFIG_MAIN);
        try {
            if (!main.exists() && main.createNewFile()) {
                createMainConfigFile(main);
            }
        } catch (IOException e) {
            Console.err("Can not create config file: " + CONFIG_FOLDER);
            Main.stop();
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
}
