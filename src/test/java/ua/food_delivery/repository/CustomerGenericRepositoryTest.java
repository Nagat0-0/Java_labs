package ua.food_delivery.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Customer Repository Tests")
public class CustomerGenericRepositoryTest {

    private GenericRepository<Customer> customerRepository;
    private Customer testCustomer1, testCustomer2, testCustomer3;

    @BeforeAll
    void setUpTestData() {
        testCustomer1 = Customer.createCustomer("Ivan", "Franko", "Lviv St 1");
        testCustomer2 = Customer.createCustomer("Lesia", "Ukrainka", "Kyiv St 2");
        testCustomer3 = Customer.createCustomer("Taras", "Shevchenko", "Kaniv St 3");
    }

    @BeforeEach
    void setUp() {
        customerRepository = new GenericRepository<>(c -> c.firstName() + " " + c.lastName(), "Customer");
        customerRepository.add(testCustomer1);
    }

    static Stream<Arguments> customerIdentityProvider() {
        return Stream.of(
                Arguments.of("Ivan Franko", true, "Valid customer ID"),
                Arguments.of("Lesia Ukrainka", true, "Another valid customer ID"),
                Arguments.of("Non Existent", false, "Non-existent customer ID"),
                Arguments.of("", false, "Empty string ID"),
                Arguments.of(null, false, "Null ID")
        );
    }

    @DisplayName("Test adding valid customers")
    @Test
    void testAddValidCustomer() {
        SoftAssertions softly = new SoftAssertions();
        int initialSize = customerRepository.size();

        boolean added = customerRepository.add(testCustomer2);

        softly.assertThat(added)
                .as("Should successfully add customer %s", testCustomer2.lastName())
                .isTrue();

        softly.assertThat(customerRepository.size())
                .as("Repository size should increase by 1")
                .isEqualTo(initialSize + 1);

        softly.assertAll();
    }

    @DisplayName("Test getting existing customer testCustomer1")
    @Test
    void testFoundCustomer() {
        String expectedId = "Ivan Franko";

        Optional<Customer> found = customerRepository.findByIdentity(expectedId);
        assertThat(found)
                .as("Should find added customer by ID %s", expectedId)
                .isPresent();

        Customer foundCustomer = found.get();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(found.isPresent())
                .as("Should find added customer by ID %s", expectedId)
                .isTrue();

        softly.assertThat(foundCustomer.firstName())
                .as("Customer first name should match")
                .isEqualTo(testCustomer1.firstName());

        softly.assertThat(foundCustomer.lastName())
                .as("Customer last name should match")
                .isEqualTo(testCustomer1.lastName());

        softly.assertThat(foundCustomer.address())
                .as("Customer address should match")
                .isEqualTo(testCustomer1.address());

        softly.assertAll();
    }

    @Test
    @DisplayName("Test duplicate prevention")
    void testDuplicatePrevention() {
        SoftAssertions softly = new SoftAssertions();

        Customer duplicateIdCustomer = Customer.createCustomer("Ivan", "Franko", "Different Address");

        boolean added = customerRepository.add(duplicateIdCustomer);

        softly.assertThat(added)
                .as("Adding customer with duplicate ID should return false")
                .isFalse();

        softly.assertThat(customerRepository.size())
                .as("Repository should still contain only one customer")
                .isEqualTo(1);

        softly.assertAll();
    }

    @Test
    @DisplayName("Test null adding prevention")
    void testNullPrevention() {
        assertThatThrownBy(() -> customerRepository.add(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("null");
    }

    @ParameterizedTest(name = "Find by ID: {0} (should find: {1}) - {2}")
    @MethodSource("customerIdentityProvider")
    @DisplayName("Test finding customers by ID")
    void testFindByIdentity(String customerId, boolean shouldFind, String description) {
        if (shouldFind && "Lesia Ukrainka".equals(customerId)) {
            customerRepository.add(testCustomer2);
        }

        SoftAssertions softly = new SoftAssertions();
        Optional<Customer> result = customerRepository.findByIdentity(customerId);

        softly.assertThat(result.isPresent())
                .as("Find result for %s should be %s", description, shouldFind ? "present" : "absent")
                .isEqualTo(shouldFind);

        if (shouldFind && result.isPresent()) {
            String foundId = result.get().firstName() + " " + result.get().lastName();
            softly.assertThat(foundId)
                    .as("Found customer should have correct ID")
                    .isEqualTo(customerId);
        }

        softly.assertAll();
    }

    @Test
    @DisplayName("Test getAll operation")
    void testGetAllCustomers() {
        SoftAssertions softly = new SoftAssertions();

        GenericRepository<Customer> emptyRepository = new GenericRepository<>(c -> c.firstName() + " " + c.lastName(), "Customer");
        List<Customer> emptyList = emptyRepository.getAll();
        softly.assertThat(emptyList)
                .as("Initially should return empty list")
                .isEmpty();

        customerRepository.add(testCustomer2);
        customerRepository.add(testCustomer3);

        List<Customer> allCustomers = customerRepository.getAll();

        softly.assertThat(allCustomers)
                .as("Should return all added customers")
                .hasSize(3)
                .contains(testCustomer1, testCustomer2, testCustomer3);

        allCustomers.clear();

        softly.assertThat(customerRepository.size())
                .as("Repository size should not be affected by external list modification")
                .isEqualTo(3);

        softly.assertAll();
    }

    @DisplayName("Test removing customers by identity")
    @Test
    void testRemoveByIdentity() {
        SoftAssertions softly = new SoftAssertions();
        int initialSize = customerRepository.size();

        boolean removed = customerRepository.removeByIdentity("Ivan Franko");

        softly.assertThat(removed)
                .as("Should successfully remove customer Ivan Franko")
                .isTrue();

        softly.assertThat(customerRepository.size())
                .as("Repository size should decrease by 1")
                .isEqualTo(initialSize - 1);

        softly.assertAll();
    }

    @Test
    @DisplayName("Test removing non-existent customer")
    void testRemoveNonExistentCustomer() {
        SoftAssertions softly = new SoftAssertions();
        int initialSize = customerRepository.size();

        boolean removed = customerRepository.removeByIdentity("Non Existent");

        softly.assertThat(removed)
                .as("Should not remove non-existent customer")
                .isFalse();

        softly.assertThat(customerRepository.size())
                .as("Repository size should remain unchanged")
                .isEqualTo(initialSize);

        softly.assertAll();
    }

    @Test
    @DisplayName("Test removing with null identity")
    void testRemoveNullIdentity() {
        SoftAssertions softly = new SoftAssertions();
        int initialSize = customerRepository.size();

        boolean removed = customerRepository.removeByIdentity(null);

        softly.assertThat(removed)
                .as("Should not remove with null identity")
                .isFalse();

        softly.assertThat(customerRepository.size())
                .as("Repository size should remain unchanged")
                .isEqualTo(initialSize);

        softly.assertAll();
    }

    @Test
    @DisplayName("Test clear operation")
    void testClearRepository() {
        SoftAssertions softly = new SoftAssertions();

        customerRepository.add(testCustomer2);
        customerRepository.add(testCustomer3);

        softly.assertThat(customerRepository.size())
                .as("Should have 3 customers before clear")
                .isEqualTo(3);

        customerRepository.clear();

        softly.assertThat(customerRepository.size())
                .as("Repository size should be 0 after clear")
                .isEqualTo(0);

        softly.assertThat(customerRepository.isEmpty())
                .as("Repository should be empty after clear")
                .isTrue();

        softly.assertThat(customerRepository.getAll())
                .as("GetAll should return empty list after clear")
                .isEmpty();

        softly.assertAll();
    }
}