package ua.food_delivery.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ua.food_delivery.exception.InvalidDataException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Restaurant Model Tests")
class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant("Valid Name", CuisineType.ITALIAN, "Valid Location");
    }

    @Nested
    @DisplayName("Constructor Validation")
    class ConstructorTests {
        @Test
        @DisplayName("Create valid restaurant")
        void testValidCreation() {
            Restaurant r = Restaurant.createRestaurant("Sushi", CuisineType.JAPANESE, "Kyiv Center");
            assertEquals("Sushi", r.getName());
        }

        @Test
        @DisplayName("Throw exception when creating invalid restaurant")
        void testInvalidCreation() {
            InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
                    Restaurant.createRestaurant("", null, "")
            );
            assertTrue(ex.getMessage().contains("name") ||
                    ex.getMessage().contains("Cuisine") ||
                    ex.getMessage().contains("Location"));
        }
    }

    @Nested
    @DisplayName("Setter Validation with Rollback")
    class SetterTests {

        @Test
        @DisplayName("Set valid name")
        void testSetValidName() {
            restaurant.setName("New Name");
            assertEquals("New Name", restaurant.getName());
        }

        @Test
        @DisplayName("Set invalid name should throw and rollback")
        void testSetInvalidName() {
            String originalName = restaurant.getName();

            assertThrows(InvalidDataException.class, () -> restaurant.setName(""));

            assertEquals(originalName, restaurant.getName());
        }

        @Test
        @DisplayName("Set null cuisine should throw and rollback")
        void testSetNullCuisine() {
            CuisineType originalCuisine = restaurant.getCuisineType();

            assertThrows(InvalidDataException.class, () -> restaurant.setCuisineType(null));

            assertEquals(originalCuisine, restaurant.getCuisineType());
        }
    }
}