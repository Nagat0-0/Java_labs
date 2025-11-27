package ua.food_delivery.serializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.util.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractDataSerializer<T> implements DataSerializer<T> {

    private static final Logger logger = LoggerUtil.getLogger();
    protected final ObjectMapper objectMapper;

    protected AbstractDataSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void serialize(List<T> items, String filePath) throws DataSerializationException {
        validateItemsForSerialization(items);
        validateFilePath(filePath);

        try {
            File file = new File(filePath);
            createParentDirectories(file);

            objectMapper.writeValue(file, items);
            logger.info("Successfully serialized " + items.size() + " items to " + getFormat() + " file: " + filePath);

        } catch (IOException e) {
            String errorMsg = String.format("Failed to serialize data to %s file: %s", getFormat(), filePath);
            throw new DataSerializationException(errorMsg, e);
        }
    }

    @Override
    public List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException {
        validateFilePath(filePath);
        validateClass(clazz);

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                logger.warning("File does not exist: " + filePath + ". Returning empty list.");
                return new ArrayList<>();
            }

            if (file.length() == 0) {
                logger.warning("File is empty: " + filePath + ". Returning empty list.");
                return new ArrayList<>();
            }

            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> items = objectMapper.readValue(file, type);

            if (items == null) {
                items = new ArrayList<>();
            }

            logger.info("Successfully deserialized " + items.size() + " items from " + getFormat() + " file: " + filePath);
            return items;

        } catch (IOException e) {
            String errorMsg = String.format("Failed to deserialize data from %s file: %s", getFormat(), filePath);
            throw new DataSerializationException(errorMsg, e);
        }
    }

    protected void validateItemsForSerialization(List<T> items) throws DataSerializationException {
        if (items == null) {
            throw new DataSerializationException("Cannot serialize null list");
        }
    }

    protected void validateFilePath(String filePath) throws DataSerializationException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new DataSerializationException("File path cannot be null or empty");
        }
    }

    protected void validateClass(Class<T> clazz) throws DataSerializationException {
        if (clazz == null) {
            throw new DataSerializationException("Class type cannot be null");
        }
    }

    protected void createParentDirectories(File file) {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                logger.info("Created directory: " + parentDir.getAbsolutePath());
            }
        }
    }
}