package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testValidCustomer() {
        Customer c = new Customer("Ivan", "Franko", "Lviv, Shevchenka St 10");
        assertEquals("Ivan", c.firstName());
    }

    @Test
    void testInvalidNameThrowsException() {
        assertThrows(InvalidDataException.class, () ->
                new Customer("", "Franko", "Lviv"));
    }

    @Test
    void testInvalidAddressThrowsException() {
        assertThrows(InvalidDataException.class, () ->
                new Customer("Ivan", "Franko", ""));
    }
}
