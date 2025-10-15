package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void testValidMenuItem() {
        MenuItem m = new MenuItem("Burger", 120.5, "Fast Food");
        assertEquals("Burger", m.getName());
    }

    @Test
    void testInvalidPriceThrowsException() {
        assertThrows(InvalidDataException.class, () ->
                new MenuItem("Burger", -5, "Fast Food"));
    }

    @Test
    void testEqualsAndHashCode() {
        MenuItem m1 = new MenuItem("Pizza", 100, "Main");
        MenuItem m2 = new MenuItem("Pizza", 100, "Main");
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }
}
