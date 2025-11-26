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
    private Customer c1_IvanZebra;
    private Customer c2_AnnaApple;
    private Customer c3_PetroBorets;

    @BeforeAll
    void setUpTestData() {
        c1_IvanZebra = Customer.createCustomer("Ivan", "Zebra", "Kyiv, Short St.");
        c2_AnnaApple = Customer.createCustomer("Anna", "Apple", "Lviv, Very Long Street Name");
        c3_PetroBorets = Customer.createCustomer("Petro", "Borets", "Odesa, Mid Street");
    }

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        customerRepository.add(c1_IvanZebra);
        customerRepository.add(c2_AnnaApple);
        customerRepository.add(c3_PetroBorets);
    }

    static Stream<Arguments> sortIdentityParams() {
        return Stream.of(
                Arguments.of("asc", "Anna Apple", "Ivan Zebra", "Petro Borets"),
                Arguments.of("desc", "Petro Borets", "Ivan Zebra", "Anna Apple"),
                Arguments.of("invalid_input", "Anna Apple", "Ivan Zebra", "Petro Borets")
        );
    }

    @DisplayName("Test Internal Sort By Identity (Generic functionality)")
    @ParameterizedTest(name = "Order ''{0}'': Expect [{1}, {2}, {3}]")
    @MethodSource("sortIdentityParams")
    void testInternalSortByIdentity(String order, String firstId, String secondId, String thirdId) {
        customerRepository.sortByIdentity(order);
        List<Customer> items = customerRepository.getAll();

        assertAll("Checking internal identity sort",
                () -> assertEquals(3, items.size()),
                () -> assertEquals(firstId, items.get(0).firstName() + " " + items.get(0).lastName()),
                () -> assertEquals(secondId, items.get(1).firstName() + " " + items.get(1).lastName()),
                () -> assertEquals(thirdId, items.get(2).firstName() + " " + items.get(2).lastName())
        );
    }

    @Test
    @DisplayName("Test External Sort by Name (Natural Order)")
    void testExternalSortByName() {

        List<Customer> sorted = customerRepository.sortByName();

        assertAll("Checking external sort by Name",
                () -> assertEquals(c2_AnnaApple, sorted.get(0), "First should be Apple"),
                () -> assertEquals(c3_PetroBorets, sorted.get(1), "Second should be Borets"),
                () -> assertEquals(c1_IvanZebra, sorted.get(2), "Third should be Zebra"),

                () -> assertEquals(3, customerRepository.size(), "Repository size must not change"),
                () -> assertNotSame(sorted, customerRepository.getAll(), "Returned list must be a copy")
        );
    }

    @Test
    @DisplayName("Test External Sort by Address Length")
    void testExternalSortByAddressLength() {

        List<Customer> sorted = customerRepository.sortByAddressLength();

        assertAll("Checking address length sort",
                () -> assertEquals(c1_IvanZebra, sorted.get(0), "Shortest address first"),
                () -> assertEquals(c3_PetroBorets, sorted.get(1), "Medium address second"),
                () -> assertEquals(c2_AnnaApple, sorted.get(2), "Longest address last")
        );
    }

    @Test
    @DisplayName("Test Sorting on Empty Repository")
    void testEmptyRepositorySorting() {
        CustomerRepository emptyRepo = new CustomerRepository();

        assertAll("Empty repo checks",
                () -> assertDoesNotThrow(() -> emptyRepo.sortByIdentity("asc")),
                () -> assertTrue(emptyRepo.sortByName().isEmpty()),
                () -> assertTrue(emptyRepo.sortByAddressLength().isEmpty())
        );
    }


    @ParameterizedTest
    @CsvSource({
            "Kyiv, 1",
            "Street, 2",
            "St, 3",
            "Berlin, 0"
    })
    @DisplayName("Stream: Find by address fragment")
    void testFindByAddressContaining(String fragment, int expectedCount) {
        List<Customer> result = customerRepository.findByAddressContaining(fragment);
        assertEquals(expectedCount, result.size(), "Failed search for: " + fragment);
    }

    @Test
    @DisplayName("Stream: Get all full names uppercase")
    void testGetAllFullNamesUpperCase() {
        List<String> names = customerRepository.getAllFullNamesUpperCase();

        assertAll("Checking Uppercase Mapping",
                () -> assertEquals(3, names.size()),
                () -> assertTrue(names.contains("ANNA APPLE")),
                () -> assertTrue(names.contains("IVAN ZEBRA")),
                () -> assertTrue(names.contains("PETRO BORETS"))
        );
    }
}