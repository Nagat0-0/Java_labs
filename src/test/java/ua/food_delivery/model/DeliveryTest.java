package ua.food_delivery.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.food_delivery.exception.InvalidDataException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Delivery Model Tests")
class DeliveryTest {

    @Test
    @DisplayName("Create valid delivery")
    void testValidDelivery() {
        Customer c = Customer.createCustomer("Ivan", "Franko", "Lviv St 10");
        MenuItem m = new MenuItem("Pizza", 100, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.CONFIRMED);

        Delivery d = Delivery.createDelivery(order, "Petro", LocalDateTime.now().plusHours(1));

        assertThat(d.deliveryPerson()).isEqualTo("Petro");
    }

    @Test
    @DisplayName("Throw exception for invalid delivery person")
    void testInvalidDeliveryPerson() {
        Customer c = Customer.createCustomer("Ivan", "Franko", "Lviv St 10");
        MenuItem m = new MenuItem("Pizza", 100, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.CONFIRMED);

        assertThatThrownBy(() -> Delivery.createDelivery(order, "", LocalDateTime.now()))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("Delivery person");
    }

    @Test
    @DisplayName("Throw exception for past delivery time")
    void testPastDeliveryTime() {
        Customer c = Customer.createCustomer("Ivan", "Franko", "Lviv St 10");
        MenuItem m = new MenuItem("Pizza", 100, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.CONFIRMED);

        assertThatThrownBy(() -> Delivery.createDelivery(order, "Petro", LocalDateTime.now().minusDays(1)))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("Delivery time");
    }
}