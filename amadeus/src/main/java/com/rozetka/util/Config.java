package com.rozetka.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final static Logger syslog = LoggerFactory.getLogger(Config.class);

    public final static String BROWSER = "browser";
    private final static Properties PROPERTIES = new Properties();

    static {
        File configFile = new File("config.properties");
        if (!configFile.exists()) {
            syslog.error(configFile.getName() + " not found");
            System.exit(1);
        }

        try (FileReader fileReader = new FileReader(configFile)) {
            PROPERTIES.load(fileReader);
        } catch (IOException e) {
            syslog.error("Error occurred during loading properties", e);
            System.exit(1);
        }

        syslog.info(" --- Config Properties ---");
        for (String key : PROPERTIES.stringPropertyNames()) {
            syslog.info("{}={}", key, get(key));
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
