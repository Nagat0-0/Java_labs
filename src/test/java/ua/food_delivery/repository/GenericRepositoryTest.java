package ua.food_delivery.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.food_delivery.model.Customer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

        assertTrue(added);
        Optional<Customer> found = customerRepo.findByIdentity("John Doe");

        assertTrue(found.isPresent());
        assertEquals(c1, found.get());
    }

    @Test
    void testFindByIdentityNotFound() {
        customerRepo.add(c1);
        Optional<Customer> found = customerRepo.findByIdentity("Non Existent");

        assertTrue(found.isEmpty());
    }

    @Test
    void testAddDuplicate() {
        boolean addedFirst = customerRepo.add(c1);

        Customer c1Duplicate = Customer.createCustomer("John", "Doe", "Different Address");

        boolean addedSecond = customerRepo.add(c1Duplicate);

        assertTrue(addedFirst);
        assertFalse(addedSecond);
        assertEquals(1, customerRepo.getAll().size());
    }

    @Test
    void testAddNull() {
        boolean added = customerRepo.add(null);
        assertFalse(added);
        assertEquals(0, customerRepo.getAll().size());
    }

    @Test
    void testRemove() {
        customerRepo.add(c1);
        customerRepo.add(c2);

        assertTrue(customerRepo.findByIdentity("Jane Smith").isPresent());

        boolean removed = customerRepo.remove(c2);

        assertTrue(removed);
        assertTrue(customerRepo.findByIdentity("Jane Smith").isEmpty());
        assertEquals(1, customerRepo.getAll().size());
        assertTrue(customerRepo.findByIdentity("John Doe").isPresent());
    }

    @Test
    void testRemoveNonExistent() {
        customerRepo.add(c1);

        boolean removed = customerRepo.remove(c2);

        assertFalse(removed);
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