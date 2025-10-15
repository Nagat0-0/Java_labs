package ua.food_delivery.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerUtilsTest {

    @Test
    void testIsValidName() {
        assertTrue(CustomerUtils.isValidName("Ivan"));
        assertFalse(CustomerUtils.isValidName(""));
    }

    @Test
    void testIsValidAddress() {
        assertTrue(CustomerUtils.isValidAddress("Kyiv, Khreshchatyk 22"));
        assertFalse(CustomerUtils.isValidAddress("A"));
    }

    @Test
    void testCapitalizeText() {
        assertEquals("Ivan", CustomerUtils.capitalizeText("ivan"));
        assertEquals("Ivan", CustomerUtils.capitalizeText(" IVAN "));
        assertNull(CustomerUtils.capitalizeText(null));
        assertEquals("", CustomerUtils.capitalizeText(""));
    }
}
