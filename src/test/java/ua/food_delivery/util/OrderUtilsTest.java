package ua.food_delivery.util;

import ua.food_delivery.model.MenuItem;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderUtilsTest {

    @Test
    void testIsValidItems() {
        assertTrue(OrderUtils.isValidItems(List.of(new MenuItem("Pizza", 100, "Main"))));
        assertFalse(OrderUtils.isValidItems(List.of()));
        assertFalse(OrderUtils.isValidItems(null));
    }
}
