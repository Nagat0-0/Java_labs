package ua.food_delivery.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ua.food_delivery.model.Customer;
import ua.food_delivery.model.MenuItem;
import ua.food_delivery.model.Order;
import ua.food_delivery.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Order Repository Tests")
class OrderRepositoryTest {

    private OrderRepository orderRepository;
    private Order o1_Old_Small;
    private Order o2_New_Big;
    private Order o3_Mid_Medium;

    @BeforeAll
    void setUpTestData() {
        Customer c = Customer.createCustomer("Test", "User", "Address 1");
        MenuItem item = MenuItem.createMenuItem("Item", 10, "Cat");

        o1_Old_Small = Order.createOrder(c, List.of(item),
                LocalDateTime.now().minusDays(10), OrderStatus.DELIVERED);

        o2_New_Big = Order.createOrder(c, List.of(item, item, item),
                LocalDateTime.now(), OrderStatus.PENDING);

        o3_Mid_Medium = Order.createOrder(c, List.of(item, item),
                LocalDateTime.now().minusDays(5), OrderStatus.PREPARING);
    }

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        orderRepository.add(o1_Old_Small);
        orderRepository.add(o2_New_Big);
        orderRepository.add(o3_Mid_Medium);
    }


    @Test
    @DisplayName("Test Sort by Date DESC")
    void testSortByDateDesc() {
        List<Order> sorted = orderRepository.sortByDateDesc();

        assertAll("Checking Date DESC sorting",
                () -> assertEquals(o2_New_Big, sorted.get(0), "Newest should be first"),
                () -> assertEquals(o3_Mid_Medium, sorted.get(1), "Middle date should be second"),
                () -> assertEquals(o1_Old_Small, sorted.get(2), "Oldest should be last")
        );
    }

    @Test
    @DisplayName("Test Sort by Items Count")
    void testSortByItemsCount() {
        List<Order> sorted = orderRepository.sortByItemsCount();

        assertAll("Checking Items Count sorting",
                () -> assertEquals(1, sorted.get(0).getItems().size()),
                () -> assertEquals(2, sorted.get(1).getItems().size()),
                () -> assertEquals(3, sorted.get(2).getItems().size())
        );
    }

    @Test
    @DisplayName("Stream: Find by Status")
    void testFindByStatus() {
        List<Order> pending = orderRepository.findByStatus(OrderStatus.PENDING);

        assertAll("Checking find by status",
                () -> assertEquals(1, pending.size()),
                () -> assertEquals(o2_New_Big, pending.get(0))
        );
    }

    @Test
    @DisplayName("Stream: Calculate Total Revenue")
    void testCalculateTotalRevenue() {

        double total = orderRepository.calculateTotalRevenue();
        assertEquals(60.0, total, 0.001);
    }
}