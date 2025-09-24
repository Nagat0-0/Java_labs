package ua.food_delivery.model;

import ua.food_delivery.util.OrderUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Customer customer;
    private List<MenuItem> items;
    private LocalDateTime orderDate;

    public Order() {
    }

    public Order(Customer customer, List<MenuItem> items, LocalDateTime orderDate) {
        setCustomer(customer);
        setItems(items);
        setOrderDate(orderDate);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer != null) {
            this.customer = customer;
        }
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        if (OrderUtils.isValidItems(items)) {
            this.items = items;
        }
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        if (orderDate != null) {
            this.orderDate = orderDate;
        }
    }

    public static Order createOrder(Customer customer, List<MenuItem> items, LocalDateTime orderDate) {
        if (customer != null && OrderUtils.isValidItems(items) && orderDate != null) {
            return new Order(customer, items, orderDate);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", items=" + items +
                ", orderDate=" + orderDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(customer, order.customer) &&
                Objects.equals(items, order.items) &&
                Objects.equals(orderDate, order.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, items, orderDate);
    }
}
