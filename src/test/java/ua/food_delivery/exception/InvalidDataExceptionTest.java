package ua.food_delivery.exception;

import ua.food_delivery.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidDataExceptionTest {

    @Test
    void testMessageConstructor() {
        InvalidDataException ex = new InvalidDataException("Error message");
        assertEquals("Error message", ex.getMessage());
    }

    @Test
    void testMessageAndCauseConstructor() {
        Throwable cause = new RuntimeException("Cause");
        InvalidDataException ex = new InvalidDataException("Error", cause);
        assertEquals("Error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
