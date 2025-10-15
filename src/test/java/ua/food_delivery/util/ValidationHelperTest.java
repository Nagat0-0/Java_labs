package ua.food_delivery.util;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationHelperTest {

    @Test
    void testIsStringLengthBetween() {
        assertTrue(ValidationHelper.isStringLengthBetween("Hello", 1, 10));
        assertFalse(ValidationHelper.isStringLengthBetween("", 1, 10));
        assertFalse(ValidationHelper.isStringLengthBetween(null, 1, 10));
    }

    @Test
    void testIsNumberBetween() {
        assertTrue(ValidationHelper.isNumberBetween(5, 1, 10));
        assertFalse(ValidationHelper.isNumberBetween(-1, 0, 5));
    }

    @Test
    void testIsStringMatchPattern() {
        assertTrue(ValidationHelper.isStringMatchPattern("abc123", "[a-z0-9]+"));
        assertFalse(ValidationHelper.isStringMatchPattern("ABC!", "[a-z0-9]+"));
        assertFalse(ValidationHelper.isStringMatchPattern(null, "[a-z]+"));
    }

    @Test
    void testIsValidList() {
        assertTrue(ValidationHelper.isValidList(List.of(1, 2, 3), 1));
        assertFalse(ValidationHelper.isValidList(List.of(), 1));
        assertFalse(ValidationHelper.isValidList(null, 1));
    }
}
