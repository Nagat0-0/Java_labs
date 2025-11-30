package ua.food_delivery.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order implements Comparable<Order> {
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    @NotNull(message = "Customer cannot be null")
    private Customer customer;

    @NotEmpty(message = "Order must have at least one item")
    private List<MenuItem> items;

    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate;

    @NotNull(message = "Status cannot be null")
    private OrderStatus status;

    public Order() {}

    @JsonCreator
    public Order(
            @JsonProperty("customer") Customer customer,
            @JsonProperty("items") List<MenuItem> items,
            @JsonProperty("orderDate") LocalDateTime orderDate,
            @JsonProperty("status") OrderStatus status) {
        this.customer = customer;
        this.items = items;
        this.orderDate = orderDate;
        this.status = status;

        ValidationUtils.validate(this);
    }

    public static Order createOrder(Customer customer, List<MenuItem> items,
                                    LocalDateTime orderDate, OrderStatus status) {
        Order order = new Order(customer, items, orderDate, status);
        logger.info("Created valid order for {}", customer.lastName());
        return order;
    }

    public void setCustomer(Customer customer) {
        Customer old = this.customer;
        this.customer = customer;
        check(() -> this.customer = old);
    }

    public void setItems(List<MenuItem> items) {
        List<MenuItem> old = this.items;
        this.items = items;
        check(() -> this.items = old);
    }

    public void setOrderDate(LocalDateTime orderDate) {
        LocalDateTime old = this.orderDate;
        this.orderDate = orderDate;
        check(() -> this.orderDate = old);
    }

    public void setStatus(OrderStatus status) {
        OrderStatus old = this.status;
        this.status = status;
        check(() -> this.status = old);
    }

    private void check(Runnable rollback) {
        try { ValidationUtils.validate(this); }
        catch (InvalidDataException e) { rollback.run(); throw e; }
    }

    public Customer getCustomer() { return customer; }
    public List<MenuItem> getItems() { return items; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }

    @JsonIgnore
    public String getStatusDescription() {
        return status != null ? status.name() : "Unknown";
    }

    @Override public int compareTo(Order o) { return this.orderDate.compareTo(o.orderDate); }
    @Override public String toString() { return "Order{customer=" + customer + ", items=" + items + "}"; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(customer, order.customer) && Objects.equals(orderDate, order.orderDate);
    }
    @Override public int hashCode() { return Objects.hash(customer, orderDate); }
}