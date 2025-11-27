package ua.food_delivery.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ua.food_delivery.exception.InvalidDataException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Model Tests")
class CustomerTest {

    @Nested
    @DisplayName("Factory Method (Creation & Validation)")
    class FactoryTests {

        @Test
        @DisplayName("Create valid customer")
        void testCreateValidCustomer() {
            Customer customer = Customer.createCustomer("Ivan", "Franko", "Lviv St. 10");

            assertNotNull(customer);
            assertEquals("Ivan", customer.firstName());
            assertEquals("Franko", customer.lastName());
            assertEquals("Lviv St. 10", customer.address());
        }

        @ParameterizedTest
        @CsvSource({
                "'', Franko, Address 1, First name",
                "Ivan, '', Address 1, Last name",
                "Ivan, Franko, '', Address"
        })
        @DisplayName("Throw InvalidDataException for empty fields")
        void testCreateInvalidCustomer(String first, String last, String addr, String errorMsgPart) {
            InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
                    Customer.createCustomer(first, last, addr)
            );
            assertTrue(ex.getMessage().contains(errorMsgPart));
        }

        @Test
        @DisplayName("Throw exception for too short fields")
        void testShortFields() {
            InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
                    Customer.createCustomer("I", "Franko", "Address")
            );
            assertTrue(ex.getMessage().contains("First name"));
        }
    }

    @Nested
    @DisplayName("Constructor (Normalization)")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should capitalize names and trim address")
        void testNormalization() {
            Customer customer = new Customer("  ivan  ", "  franko  ", "  Lviv St. 10  ");

            assertEquals("Ivan", customer.firstName());
            assertEquals("Franko", customer.lastName());
            assertEquals("Lviv St. 10", customer.address());
        }
    }
}