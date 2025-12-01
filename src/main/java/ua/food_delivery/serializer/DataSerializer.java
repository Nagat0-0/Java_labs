package ua.food_delivery.serializer;

import ua.food_delivery.exception.DataSerializationException;
import java.util.List;

public interface DataSerializer<T> {
    void serialize(List<T> items, String filePath) throws DataSerializationException;
    List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException;
    String toString(T item) throws DataSerializationException;
    String listToString(List<T> items) throws DataSerializationException;
    T fromString(String str, Class<T> clazz) throws DataSerializationException;
    String getFormat();
}