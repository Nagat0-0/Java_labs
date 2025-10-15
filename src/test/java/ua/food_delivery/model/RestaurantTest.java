package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void testValidRestaurantCreation() {
        Restaurant r = new Restaurant("La Strada", CuisineType.ITALIAN, "Kyiv, Khreshchatyk 10");
        assertEquals("La Strada", r.getName());
        assertEquals(CuisineType.ITALIAN, r.getCuisineType());
        assertEquals("Kyiv, Khreshchatyk 10", r.getLocation());
    }

    @Test
    void testInvalidNameThrowsException() {
        assertThrows(InvalidDataException.class, () ->
                new Restaurant("", CuisineType.ITALIAN, "Lviv, Shevchenka 5"));
    }

    @Test
    void testInvalidCuisineTypeThrowsException() {
        assertThrows(InvalidDataException.class, () ->
                new Restaurant("Gastro Bar", null, "Lviv, Horodotska 11"));
    }

    @Test
    void testInvalidLocationThrowsException() {
        assertThrows(InvalidDataException.class, () ->
                new Restaurant("Mister Sushi", CuisineType.JAPANESE, ""));
    }

    @Test
    void testEqualsAndHashCode() {
        Restaurant r1 = new Restaurant("La Strada", CuisineType.ITALIAN, "Kyiv, Khreshchatyk 10");
        Restaurant r2 = new Restaurant("La Strada", CuisineType.ITALIAN, "Kyiv, Khreshchatyk 10");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
