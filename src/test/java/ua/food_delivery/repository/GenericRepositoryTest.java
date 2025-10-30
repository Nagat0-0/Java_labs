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


        c1 = Customer.createCustomer("Тест", "Один", "Адреса 1");
        c2 = Customer.createCustomer("Тест", "Два", "Адреса 2");
    }

    @Test
    void testAddAndFindByIdentity() {
        boolean added = customerRepo.add(c1);

        assertTrue(added, "Додавання має повернути true");
        Optional<Customer> found = customerRepo.findByIdentity("Тест Один");

        assertTrue(found.isPresent(), "Клієнт має бути знайдений");
        assertEquals(c1, found.get(), "Знайдений клієнт має відповідати доданому");
    }

    @Test
    void testFindByIdentityNotFound() {
        customerRepo.add(c1);
        Optional<Customer> found = customerRepo.findByIdentity("Неіснуючий Клієнт");

        assertTrue(found.isEmpty(), "Optional має бути порожнім");
    }

    @Test
    void testAddDuplicate() {
        boolean addedFirst = customerRepo.add(c1);

        Customer c1Duplicate = Customer.createCustomer("Тест", "Один", "Інша Адреса 3");

        boolean addedSecond = customerRepo.add(c1Duplicate);

        assertTrue(addedFirst, "Перше додавання має бути успішним");
        assertFalse(addedSecond, "Додавання дублікату має повернути false");
        assertEquals(1, customerRepo.getAll().size(), "Репозиторій має містити лише один екземпляр");
    }

    @Test
    void testAddNull() {
        boolean added = customerRepo.add(null);
        assertFalse(added, "Додавання null має повернути false");
        assertEquals(0, customerRepo.getAll().size(), "Репозиторій має бути порожнім");
    }

    @Test
    void testRemove() {
        customerRepo.add(c1);
        customerRepo.add(c2);

        assertTrue(customerRepo.findByIdentity("Тест Два").isPresent());

        boolean removed = customerRepo.remove(c2);

        assertTrue(removed, "Видалення має повернути true");
        assertTrue(customerRepo.findByIdentity("Тест Два").isEmpty(), "Клієнт c2 має бути видалений");
        assertEquals(1, customerRepo.getAll().size(), "Розмір репозиторію має бути 1");
        assertTrue(customerRepo.findByIdentity("Тест Один").isPresent(), "Клієнт c1 має залишитись");
    }

    @Test
    void testRemoveNonExistent() {
        customerRepo.add(c1);

        boolean removed = customerRepo.remove(c2);

        assertFalse(removed, "Видалення неіснуючого об'єкта має повернути false");
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