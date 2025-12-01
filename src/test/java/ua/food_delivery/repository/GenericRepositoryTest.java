package ua.food_delivery.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.Customer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenericRepositoryTest {

    private GenericRepository<Customer> customerRepo;
    private Customer c1;
    private Customer c2;

    @BeforeEach
    void setUp() {
        customerRepo = new GenericRepository<>(
                c -> c.firstName() + " " + c.lastName(),
                "TestCustomer"
        );

        c1 = Customer.createCustomer("John", "Doe", "Address One");
        c2 = Customer.createCustomer("Jane", "Smith", "Address Two");
    }

    @Test
    void testAddAndFindByIdentity() {
        boolean added = customerRepo.add(c1);

        assertTrue(added, "Adding should return true");
        Optional<Customer> found = customerRepo.findByIdentity("John Doe");

        assertTrue(found.isPresent(), "Customer should be found");
        assertEquals(c1, found.get(), "Found customer should match added");
    }

    @Test
    void testFindByIdentityNotFound() {
        customerRepo.add(c1);
        Optional<Customer> found = customerRepo.findByIdentity("Non Existent");

        assertTrue(found.isEmpty(), "Optional should be empty");
    }

    @Test
    void testAddDuplicate() {
        boolean addedFirst = customerRepo.add(c1);

        Customer c1Duplicate = Customer.createCustomer("John", "Doe", "Address Three");

        boolean addedSecond = customerRepo.add(c1Duplicate);

        assertTrue(addedFirst, "First add should succeed");
        assertFalse(addedSecond, "Adding duplicate should return false");
        assertEquals(1, customerRepo.getAll().size(), "Repository should contain only one item");
    }

    @Test
    void testAddNull() {
        assertThatThrownBy(() -> customerRepo.add(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("null");
    }

    @Test
    void testRemove() {
        customerRepo.add(c1);
        customerRepo.add(c2);

        assertTrue(customerRepo.findByIdentity("Jane Smith").isPresent());

        boolean removed = customerRepo.remove(c2);

        assertTrue(removed, "Remove should return true");
        assertTrue(customerRepo.findByIdentity("Jane Smith").isEmpty(), "c2 should be removed");
        assertEquals(1, customerRepo.getAll().size(), "Size should be 1");
        assertTrue(customerRepo.findByIdentity("John Doe").isPresent(), "c1 should remain");
    }

    @Test
    void testRemoveNonExistent() {
        customerRepo.add(c1);

        boolean removed = customerRepo.remove(c2);

        assertFalse(removed, "Removing non-existent item should return false");
        assertEquals(1, customerRepo.getAll().size());
    }

    @Test
    void testGetAll() {
        customerRepo.add(c1);
        customerRepo.add(c2);

        List<Customer> allItems = customerRepo.getAll();
        assertEquals(2, allItems.size());
        assertTrue(allItems.containsAll(List.of(c1, c2)));
    }
}