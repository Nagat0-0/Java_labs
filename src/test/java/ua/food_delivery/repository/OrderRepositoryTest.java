package ua.food_delivery.repository;

import org.junit.jupiter.api.*;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.Customer;
import ua.food_delivery.model.MenuItem;
import ua.food_delivery.model.Order;
import ua.food_delivery.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Order Repository Tests")
class OrderRepositoryTest {

    private OrderRepository repo;
    private Order o1, o2;

    @BeforeAll
    void setUpTestData() {
        Customer c = Customer.createCustomer("Test", "User", "Valid Address 123");
        MenuItem m1 = MenuItem.createMenuItem("Pizza", 100.0, "Food");
        MenuItem m2 = MenuItem.createMenuItem("Cola", 50.0, "Drink");

        o1 = Order.createOrder(c, List.of(m1), LocalDateTime.now().minusDays(1), OrderStatus.DELIVERED);
        o2 = Order.createOrder(c, List.of(m1, m2), LocalDateTime.now(), OrderStatus.PENDING);
    }

    @BeforeEach
    void setUp() {
        repo = new OrderRepository();
        repo.add(o1);
        repo.add(o2);
    }

    @Test
    @DisplayName("Adding null order throws InvalidDataException")
    void testAddNull() {
        assertThatThrownBy(() -> repo.add(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    @DisplayName("Sort by Date DESC")
    void testSortByDateDesc() {
        List<Order> sorted = repo.sortByDateDesc();

        assertAll(
                () -> assertThat(sorted.get(0)).isEqualTo(o2),
                () -> assertThat(sorted.get(1)).isEqualTo(o1)
        );
    }

    @Test
    @DisplayName("Sort by Items Count")
    void testSortByItemsCount() {
        List<Order> sorted = repo.sortByItemsCount();

        assertAll(
                () -> assertThat(sorted.get(0).getItems()).hasSize(1),
                () -> assertThat(sorted.get(1).getItems()).hasSize(2)
        );
    }

    @Test
    @DisplayName("Stream: Find by Status")
    void testFindByStatus() {
        List<Order> pending = repo.findByStatus(OrderStatus.PENDING);

        assertThat(pending).hasSize(1);
        assertThat(pending.get(0)).isEqualTo(o2);
    }

    @Test
    @DisplayName("Stream: Calculate Total Revenue")
    void testCalculateTotalRevenue() {
        double total = repo.calculateTotalRevenue();
        assertThat(total).isEqualTo(250.0);
    }
}