package ua.food_delivery.serializer;

import ua.food_delivery.exception.DataSerializationException;
import java.util.List;

public interface DataSerializer<T> {
    void serialize(List<T> items, String filePath) throws DataSerializationException;
    List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException;
    String getFormat();
}