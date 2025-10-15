package ua.food_delivery.util;

import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LoggerUtilTest {

    @Test
    void testGetLoggerReturnsNonNull() {
        Logger logger = LoggerUtil.getLogger();
        assertNotNull(logger);
        assertEquals("FoodDeliveryLogger", logger.getName());
    }
}
