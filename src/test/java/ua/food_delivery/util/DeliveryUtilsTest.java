package ua.food_delivery.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryUtilsTest {

    @Test
    void testIsValidName() {
        assertTrue(DeliveryUtils.isValidName("Petro"));
        assertFalse(DeliveryUtils.isValidName(""));
    }

    @Test
    void testCapitalizeText() {
        assertEquals("Petro", DeliveryUtils.capitalizeText("petro"));
        assertEquals("Petro", DeliveryUtils.capitalizeText(" PETRO "));
        assertNull(DeliveryUtils.capitalizeText(null));
        assertEquals("", DeliveryUtils.capitalizeText(""));
    }
}
