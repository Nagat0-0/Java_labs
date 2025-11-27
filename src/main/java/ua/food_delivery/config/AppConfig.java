package ua.food_delivery.config;

import ua.food_delivery.util.LoggerUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class AppConfig {

    private static final Logger logger = LoggerUtil.getLogger();
    private final Properties properties;
    private final String configFilePath;

    public AppConfig() {
        this("config.properties");
    }

    public AppConfig(String configFilePath) {
        this.configFilePath = configFilePath;
        this.properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
            logger.info("Configuration loaded successfully from file: " + configFilePath);
        } catch (IOException e) {
            logger.warning("Could not load config from file system: " + configFilePath + ". Trying classpath...");

            try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
                if (input == null) {
                    logger.severe("Configuration file not found in classpath: " + configFilePath);
                    throw new RuntimeException("Unable to find configuration file: " + configFilePath);
                }
                properties.load(input);
                logger.info("Configuration loaded successfully from classpath: " + configFilePath);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load configuration", ex);
            }
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getJsonFilePath(String entityType) {
        String basePath = getBaseDataPath();
        String key = String.format("data.path.%s.json", entityType.toLowerCase());
        String filename = getProperty(key);

        if (filename == null) {
            logger.warning("JSON filename not found for entity: " + entityType + ". Using default.");
            filename = String.format("%s.json", entityType.toLowerCase());
        }

        return combinePaths(basePath, filename);
    }

    public String getYamlFilePath(String entityType) {
        String basePath = getBaseDataPath();
        String key = String.format("data.path.%s.yaml", entityType.toLowerCase());
        String filename = getProperty(key);

        if (filename == null) {
            logger.warning("YAML filename not found for entity: " + entityType + ". Using default.");
            filename = String.format("%s.yaml", entityType.toLowerCase());
        }

        return combinePaths(basePath, filename);
    }

    public String getBaseDataPath() {
        return getProperty("data.path.base", "./data");
    }

    private String combinePaths(String basePath, String filename) {
        Path base = Paths.get(basePath);
        Path file = Paths.get(filename);

        if (file.isAbsolute()) {
            return filename;
        }

        return base.resolve(file).toString();
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warning("Invalid integer value for key " + key + ": " + value + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    public Properties getAllProperties() {
        return new Properties(properties);
    }
}