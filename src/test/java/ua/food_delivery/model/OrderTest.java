package ua.food_delivery.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.food_delivery.exception.InvalidDataException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Order Model Tests")
class OrderTest {

    @Test
    @DisplayName("Create valid order")
    void testValidOrder() {
        Customer c = Customer.createCustomer("Ivan", "Franko", "Address 1");
        MenuItem m = new MenuItem("Pizza", 100, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.PENDING);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Throw exception for empty item list")
    void testInvalidItems() {
        Customer c = Customer.createCustomer("Ivan", "Franko", "Address 1");

        assertThatThrownBy(() -> Order.createOrder(c, List.of(), LocalDateTime.now(), OrderStatus.PENDING))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("Order must have at least one item");
    }
}