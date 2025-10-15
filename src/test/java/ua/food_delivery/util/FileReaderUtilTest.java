package ua.food_delivery.util;

import ua.food_delivery.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderUtilTest {

    @Test
    void testReadRestaurantsFromMissingFileThrows() {
        assertThrows(InvalidDataException.class, () ->
                FileReaderUtil.readRestaurantsFromFile("missing.csv"));
    }

    @Test
    void testReadCustomersFromMissingFileThrows() {
        assertThrows(InvalidDataException.class, () ->
                FileReaderUtil.readCustomersFromFile("missing.csv"));
    }

    @Test
    void testReadMenuItemsFromMissingFileThrows() {
        assertThrows(InvalidDataException.class, () ->
                FileReaderUtil.readMenuItemsFromFile("missing.csv"));
    }
}
