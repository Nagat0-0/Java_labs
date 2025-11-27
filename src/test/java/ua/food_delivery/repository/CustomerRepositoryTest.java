package ua.food_delivery.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ua.food_delivery.model.Customer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Customer Repository Tests")
class CustomerRepositoryTest {

    private CustomerRepository customerRepository;
    private Customer c1, c2, c3;

    @BeforeAll
    void setUpTestData() {
        c1 = Customer.createCustomer("Ivan", "Zebra", "Kyiv, Short St.");
        c2 = Customer.createCustomer("Anna", "Apple", "Lviv, Very Long Street Name");
        c3 = Customer.createCustomer("Petro", "Borets", "Odesa, Mid Street");
    }

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        customerRepository.add(c1);
        customerRepository.add(c2);
        customerRepository.add(c3);
    }

    @Test
    @DisplayName("Adding null returns false (graceful handling)")
    void testAddNull() {
        boolean result = customerRepository.add(null);
        assertFalse(result);
    }

    static Stream<Arguments> sortIdentityParams() {
        return Stream.of(
                Arguments.of("asc", "Anna Apple", "Ivan Zebra", "Petro Borets"),
                Arguments.of("desc", "Petro Borets", "Ivan Zebra", "Anna Apple")
        );
    }

    @DisplayName("Test Internal Sort By Identity")
    @ParameterizedTest(name = "Order ''{0}''")
    @MethodSource("sortIdentityParams")
    void testInternalSortByIdentity(String order, String firstId, String secondId, String thirdId) {
        customerRepository.sortByIdentity(order);
        List<Customer> items = customerRepository.getAll();

        assertAll(
                () -> assertEquals(firstId, items.get(0).firstName() + " " + items.get(0).lastName()),
                () -> assertEquals(secondId, items.get(1).firstName() + " " + items.get(1).lastName())
        );
    }

    @Test
    @DisplayName("Test External Sort by Name")
    void testExternalSortByName() {
        List<Customer> sorted = customerRepository.sortByName();
        assertEquals("Apple", sorted.get(0).lastName());
        assertEquals("Zebra", sorted.get(2).lastName());
    }

    @ParameterizedTest
    @CsvSource({ "Kyiv, 1", "Street, 2", "St, 3", "Berlin, 0" })
    @DisplayName("Stream: Find by address fragment")
    void testFindByAddressContaining(String fragment, int expectedCount) {
        List<Customer> result = customerRepository.findByAddressContaining(fragment);
        assertEquals(expectedCount, result.size());
    }
}