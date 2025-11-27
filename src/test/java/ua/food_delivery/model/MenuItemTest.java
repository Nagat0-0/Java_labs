package ua.food_delivery.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ua.food_delivery.exception.InvalidDataException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("MenuItem Model Tests")
class MenuItemTest {

    @Test
    @DisplayName("Create valid item")
    void testValidItem() {
        assertDoesNotThrow(() -> MenuItem.createMenuItem("Pizza", 100.0, "Food"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.01})
    @DisplayName("Throw exception for non-positive price")
    void testInvalidPrice(double price) {
        assertThatThrownBy(() -> MenuItem.createMenuItem("Pizza", price, "Food"))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("Price");
    }

    @Test
    @DisplayName("Throw exception for excessively high price")
    void testTooHighPrice() {
        assertThatThrownBy(() -> MenuItem.createMenuItem("Pizza", 15000.0, "Food"))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("too high");
    }
}