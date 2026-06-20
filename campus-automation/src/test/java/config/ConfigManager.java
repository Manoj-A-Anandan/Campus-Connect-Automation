package config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties prop = new Properties();

    static {
        try (InputStream fis = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (fis == null) {
                throw new RuntimeException("config.properties file not found in classpath");
            }
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigManager() {
        // Private constructor to prevent instantiation
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}
