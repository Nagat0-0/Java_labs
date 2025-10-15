package ua.food_delivery.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemUtilsTest {

    @Test
    void testIsValidName() {
        assertTrue(MenuItemUtils.isValidName("Pizza"));
        assertFalse(MenuItemUtils.isValidName(""));
    }

    @Test
    void testIsValidPrice() {
        assertTrue(MenuItemUtils.isValidPrice(99.99));
        assertFalse(MenuItemUtils.isValidPrice(0));
        assertFalse(MenuItemUtils.isValidPrice(2000));
    }

    @Test
    void testIsValidCategory() {
        assertTrue(MenuItemUtils.isValidCategory("Fast Food"));
        assertFalse(MenuItemUtils.isValidCategory(""));
    }
}
