package ru.falseteam.schedule.server;


import java.io.File;

/**
 * @author Sumin Vladislav
 */
public class StaticSettings {
    private static final String CONFIG_FOLDER = "config";

    public static void main(String[] args) {
        init();
    }

    static void init() {
        Console.print("Loading settings from config files");
        final String separator = File.separator;

        File config = new File(CONFIG_FOLDER);
        if (!config.exists() && !config.mkdir()) {
            Console.err("Can not create config folder.");
            //Main.stop();
        }
    }
}
