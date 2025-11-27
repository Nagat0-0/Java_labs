package ua.food_delivery.persistence;

import ua.food_delivery.config.AppConfig;
import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.serializer.DataSerializer;
import ua.food_delivery.serializer.JsonDataSerializer;
import ua.food_delivery.serializer.YamlDataSerializer;
import ua.food_delivery.util.LoggerUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PersistenceManager {

    private static final Logger logger = LoggerUtil.getLogger();

    private final AppConfig config;
    private final Map<String, DataSerializer<?>> serializers;

    public PersistenceManager(AppConfig config) {
        this.config = config;
        this.serializers = new HashMap<>();
        initializeSerializers();
        logger.info("RepositoryManager initialized with " + serializers.size() + " serializers");
    }

    private void initializeSerializers() {
        serializers.put("JSON", new JsonDataSerializer<>());
        serializers.put("YAML", new YamlDataSerializer<>());
        logger.config("Registered serializers: " + serializers.keySet());
    }

    public <T> void save(List<T> items,
                         String entityType,
                         Class<T> clazz,
                         String format) throws DataSerializationException {
        validateParameters(items, entityType, clazz);

        String formatUpper = format.toUpperCase();
        DataSerializer<T> serializer = getSerializer(formatUpper);
        String filePath = getFilePath(entityType, formatUpper);

        logger.info("Saving " + items.size() + " items of type " + entityType + " to " + formatUpper + " file: " + filePath);

        try {
            serializer.serialize(items, filePath);
            logger.info("Successfully saved " + items.size() + " " + entityType + " items to " + formatUpper);
        } catch (DataSerializationException e) {
            logger.severe("Failed to save " + entityType + " to " + formatUpper + ": " + e.getMessage());
            throw e;
        }
    }

    public <T> List<T> load(String entityType,
                            Class<T> clazz,
                            String format) throws DataSerializationException {
        if (entityType == null || entityType.trim().isEmpty()) {
            throw new DataSerializationException("Entity type cannot be null or empty");
        }

        if (clazz == null) {
            throw new DataSerializationException("Class type cannot be null");
        }

        String formatUpper = format.toUpperCase();
        DataSerializer<T> serializer = getSerializer(formatUpper);
        String filePath = getFilePath(entityType, formatUpper);

        logger.info("Loading " + entityType + " from " + formatUpper + " file: " + filePath);

        try {
            List<T> items = serializer.deserialize(filePath, clazz);
            logger.info("Successfully loaded " + items.size() + " items of type " + entityType);
            return items;
        } catch (DataSerializationException e) {
            logger.severe("Failed to load " + entityType + " from " + formatUpper + ": " + e.getMessage());
            throw e;
        }
    }

    public <T> void saveAllFormats(List<T> items,
                                   String entityType,
                                   Class<T> clazz) throws DataSerializationException {
        logger.info("Saving " + items.size() + " items of type " + entityType + " to all formats");

        save(items, entityType, clazz, "JSON");
        save(items, entityType, clazz, "YAML");

        logger.info("Successfully saved " + entityType + " to all formats");
    }

    private <T> void validateParameters(List<T> items, String entityType, Class<T> clazz)
            throws DataSerializationException {
        if (items == null) {
            throw new DataSerializationException("Items list cannot be null");
        }

        if (entityType == null || entityType.trim().isEmpty()) {
            throw new DataSerializationException("Entity type cannot be null or empty");
        }

        if (clazz == null) {
            throw new DataSerializationException("Class type cannot be null");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> DataSerializer<T> getSerializer(String format) throws DataSerializationException {
        DataSerializer<?> serializer = serializers.get(format);

        if (serializer == null) {
            String errorMsg = String.format("Unsupported format: %s. Available formats: %s",
                    format, serializers.keySet());
            logger.severe(errorMsg);
            throw new DataSerializationException(errorMsg);
        }

        return (DataSerializer<T>) serializer;
    }

    private String getFilePath(String entityType, String format) {
        return switch (format) {
            case "JSON" -> config.getJsonFilePath(entityType);
            case "YAML" -> config.getYamlFilePath(entityType);
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }

    public boolean isFormatSupported(String format) {
        return serializers.containsKey(format.toUpperCase());
    }

    public String[] getSupportedFormats() {
        return serializers.keySet().toArray(new String[0]);
    }
}