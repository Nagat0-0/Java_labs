package ua.food_delivery.repository;

import ua.food_delivery.model.MenuItem;
import ua.food_delivery.model.Order;
import ua.food_delivery.model.OrderStatus;
import ua.food_delivery.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrderRepository extends GenericRepository<Order> {
    private static final Logger logger = LoggerUtil.getLogger();

    public OrderRepository() {
        super(o -> o.getCustomer().lastName() + "_" + o.getOrderDate(), "Order");
    }

    public List<Order> sortByDateDesc() {
        List<Order> all = getAll();
        all.sort(Comparator.reverseOrder());
        logger.info("Sorted Order by date (desc)");
        return all;
    }

    public List<Order> sortByItemsCount() {
        List<Order> all = getAll();
        all.sort(Comparator.comparingInt(o -> o.getItems().size()));
        logger.info("Sorted Order by items count");
        return all;
    }

    public List<Order> findByStatus(OrderStatus status) {
        List<Order> results = items.stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
        logger.info("Found " + results.size() + " orders with status " + status);
        return results;
    }

    public double calculateTotalRevenue() {
        double total = items.stream()
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(MenuItem::getPrice)
                .sum();

        logger.info("Total revenue calculated: " + total);
        return total;
    }
}