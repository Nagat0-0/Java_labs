package ua.food_delivery.model;

import ua.food_delivery.util.OrderUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Customer customer;
    private List<MenuItem> items;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public Order() {
    }

    public Order(Customer customer, List<MenuItem> items, LocalDateTime orderDate, OrderStatus status) {
        setCustomer(customer);
        setItems(items);
        setOrderDate(orderDate);
        setStatus(status);
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) {
        if (customer != null) this.customer = customer;
    }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) {
        if (OrderUtils.isValidItems(items)) this.items = items;
    }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) {
        if (orderDate != null) this.orderDate = orderDate;
    }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) {
        if (status != null) this.status = status;
    }

    public static Order createOrder(Customer customer, List<MenuItem> items,
                                    LocalDateTime orderDate, OrderStatus status) {
        if (customer != null && OrderUtils.isValidItems(items) && orderDate != null && status != null) {
            return new Order(customer, items, orderDate, status);
        }
        return null;
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
