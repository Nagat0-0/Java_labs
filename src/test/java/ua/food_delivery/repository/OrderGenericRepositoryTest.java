package ua.food_delivery.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.Customer;
import ua.food_delivery.model.MenuItem;
import ua.food_delivery.model.Order;
import ua.food_delivery.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Order Repository Tests")
class OrderGenericRepositoryTest {

    private GenericRepository<Order> orderRepository;
    private Order order1, order2;
    private Customer customer;
    private List<MenuItem> items;

    @BeforeAll
    void setUpTestData() {
        customer = Customer.createCustomer("Test", "User", "Address 1");
        items = List.of(MenuItem.createMenuItem("Item 1", 100.0, "Cat"));

        LocalDateTime time1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2025, 1, 2, 12, 0);

        order1 = Order.createOrder(customer, items, time1, OrderStatus.PENDING);
        order2 = Order.createOrder(customer, items, time2, OrderStatus.DELIVERED);
    }

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        orderRepository.add(order1);
    }

    private String getIdentity(Order order) {
        return order.getCustomer().lastName() + "_" + order.getOrderDate();
    }

    @DisplayName("Test adding valid order")
    @Test
    void testAddValidOrder() {
        SoftAssertions softly = new SoftAssertions();
        int initialSize = orderRepository.size();

        boolean added = orderRepository.add(order2);

        softly.assertThat(added).isTrue();
        softly.assertThat(orderRepository.size()).isEqualTo(initialSize + 1);
        softly.assertAll();
    }

    @DisplayName("Test finding existing order")
    @Test
    void testFoundOrder() {
        String identity = getIdentity(order1);
        Optional<Order> found = orderRepository.findByIdentity(identity);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(order1);
    }

    @Test
    @DisplayName("Test duplicate prevention")
    void testDuplicatePrevention() {
        boolean added = orderRepository.add(order1);
        assertThat(added).isFalse();
        assertThat(orderRepository.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test null adding prevention")
    void testNullPrevention() {
        assertThatThrownBy(() -> orderRepository.add(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("null");
    }

    @Test
    @DisplayName("Test finding orders by identity")
    void testFindByIdentity() {
        String id1 = getIdentity(order1);
        String id2 = getIdentity(order2);

        assertThat(orderRepository.findByIdentity(id1)).isPresent();
        assertThat(orderRepository.findByIdentity(id2)).isEmpty();

        orderRepository.add(order2);
        assertThat(orderRepository.findByIdentity(id2)).isPresent();
    }

    @Test
    @DisplayName("Test getAll operation")
    void testGetAllOrders() {
        orderRepository.add(order2);
        List<Order> all = orderRepository.getAll();
        assertThat(all).hasSize(2).contains(order1, order2);
    }

    @Test
    @DisplayName("Test remove by identity")
    void testRemoveByIdentity() {
        String identity = getIdentity(order1);
        boolean removed = orderRepository.removeByIdentity(identity);

        assertThat(removed).isTrue();
        assertThat(orderRepository.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test clear operation")
    void testClearRepository() {
        orderRepository.add(order2);
        orderRepository.clear();
        assertThat(orderRepository.isEmpty()).isTrue();
    }
}