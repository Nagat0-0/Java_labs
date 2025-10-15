package ua.food_delivery.util;

import ua.food_delivery.model.CuisineType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantUtilsTest {

    @Test
    void testIsValidName() {
        assertTrue(RestaurantUtils.isValidName("Sushi House"));
        assertFalse(RestaurantUtils.isValidName(""));
    }

    @Test
    void testIsValidCuisineType() {
        assertTrue(RestaurantUtils.isValidCuisineType(CuisineType.JAPANESE));
        assertFalse(RestaurantUtils.isValidCuisineType(null));
    }

    @Test
    void testIsValidLocation() {
        assertTrue(RestaurantUtils.isValidLocation("Lviv, Franka 12"));
        assertFalse(RestaurantUtils.isValidLocation(""));
    }
}
