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
            throw new DataSerializationException("Failed to serialize data", e);
        }
    }

    @Override
    public List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException {
        validateFilePath(filePath);
        validateClass(clazz);

        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> items = objectMapper.readValue(file, type);
            return items != null ? items : new ArrayList<>();
        } catch (IOException e) {
            throw new DataSerializationException("Failed to deserialize data", e);
        }
    }

    @Override
    public String toString(T item) throws DataSerializationException {
        if (item == null) throw new DataSerializationException("Cannot serialize null item");
        try {
            return objectMapper.writeValueAsString(item);
        } catch (IOException e) {
            throw new DataSerializationException("Failed to serialize item", e);
        }
    }

    @Override
    public String listToString(List<T> items) throws DataSerializationException {
        if (items == null) throw new DataSerializationException("Cannot serialize null list");
        try {
            return objectMapper.writeValueAsString(items);
        } catch (IOException e) {
            throw new DataSerializationException("Failed to serialize list", e);
        }
    }

    @Override
    public T fromString(String str, Class<T> clazz) throws DataSerializationException {
        if (str == null || str.trim().isEmpty()) throw new DataSerializationException("Cannot deserialize empty string");
        try {
            return objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            throw new DataSerializationException("Failed to deserialize string", e);
        }
    }

    protected void validateItemsForSerialization(List<T> items) throws DataSerializationException {
        if (items == null) throw new DataSerializationException("Cannot serialize null list");
    }

    protected void validateFilePath(String filePath) throws DataSerializationException {
        if (filePath == null || filePath.trim().isEmpty()) throw new DataSerializationException("File path cannot be null or empty");
    }

    protected void validateClass(Class<T> clazz) throws DataSerializationException {
        if (clazz == null) throw new DataSerializationException("Class type cannot be null");
    }

    protected void createParentDirectories(File file) {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}