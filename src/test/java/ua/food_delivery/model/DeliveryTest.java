package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    void testValidDeliveryCreation() {
        Customer c = new Customer("Ivan", "Franko", "Lviv St 10");
        MenuItem m = new MenuItem("Pizza", 100, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.CONFIRMED);

        Delivery d = new Delivery(order, "Petro", LocalDateTime.now());
        assertEquals("Petro", d.deliveryPerson());
    }

    @Test
    void testInvalidDeliveryPerson() {
        Customer c = new Customer("Ivan", "Franko", "Lviv St 10");
        MenuItem m = new MenuItem("Pizza", 100, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.CONFIRMED);

        assertThrows(InvalidDataException.class, () ->
                new Delivery(order, "", LocalDateTime.now()));
    }
}
