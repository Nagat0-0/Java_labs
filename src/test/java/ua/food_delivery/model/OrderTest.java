package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testValidOrderCreation() {
        Customer c = new Customer("Ivan", "Franko", "lvivska 25");
        MenuItem m = new MenuItem("Pizza", 150, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.PENDING);
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testInvalidItemsThrowsException() {
        Customer c = new Customer("Ivan", "Franko", "lvivska 25");
        assertThrows(InvalidDataException.class, () ->
                new Order(c, List.of(), LocalDateTime.now(), OrderStatus.PENDING));
    }

    @Test
    void testStatusDescription() {
        Customer c = new Customer("Ivan", "Franko", "lvivska 25");
        MenuItem m = new MenuItem("Pizza", 150, "Main");
        Order order = new Order(c, List.of(m), LocalDateTime.now(), OrderStatus.DELIVERED);
        assertTrue(order.getStatusDescription().contains("delivered"));
    }
}
