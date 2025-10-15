package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.OrderUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Customer customer;
    private List<MenuItem> items;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public Order() {}

    public Order(Customer customer, List<MenuItem> items,
                 LocalDateTime orderDate, OrderStatus status) throws InvalidDataException {
        setCustomer(customer);
        setItems(items);
        setOrderDate(orderDate);
        setStatus(status);
    }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) throws InvalidDataException {
        if (customer == null) throw new InvalidDataException("Customer cannot be null");
        this.customer = customer;
    }

    public List<MenuItem> getItems() { return items; }

    public void setItems(List<MenuItem> items) throws InvalidDataException {
        if (!OrderUtils.isValidItems(items)) {
            throw new InvalidDataException("Invalid items list (must contain at least one item)");
        }
        this.items = items;
    }

    public LocalDateTime getOrderDate() { return orderDate; }

    public void setOrderDate(LocalDateTime orderDate) throws InvalidDataException {
        if (orderDate == null) throw new InvalidDataException("Order date cannot be null");
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) throws InvalidDataException {
        if (status == null) throw new InvalidDataException("Order status cannot be null");
        this.status = status;
    }

    public static Order createOrder(Customer customer, List<MenuItem> items,
                                    LocalDateTime orderDate, OrderStatus status) throws InvalidDataException {
        return new Order(customer, items, orderDate, status);
    }

    public String getStatusDescription() {
        return switch (status) {
            case PENDING -> "Order is pending confirmation.";
            case CONFIRMED -> "Order has been confirmed.";
            case PREPARING -> "Order is being prepared.";
            case DELIVERED -> "Order has been delivered.";
            case CANCELED -> "Order was canceled.";
        };
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", items=" + items +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(customer, order.customer) &&
                Objects.equals(items, order.items) &&
                Objects.equals(orderDate, order.orderDate) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, items, orderDate, status);
    }
}
