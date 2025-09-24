package ua.food_delivery.model;

import ua.food_delivery.util.DeliveryUtils;

import java.time.LocalDateTime;
import java.util.Objects;

public class Delivery {
    private Order order;
    private String deliveryPerson;
    private LocalDateTime deliveryTime;

    public Delivery() {
    }

    public Delivery(Order order, String deliveryPerson, LocalDateTime deliveryTime) {
        setOrder(order);
        setDeliveryPerson(deliveryPerson);
        setDeliveryTime(deliveryTime);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order != null) {
            this.order = order;
        }
    }

    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(String deliveryPerson) {
        if (DeliveryUtils.isValidName(deliveryPerson)) {
            this.deliveryPerson = DeliveryUtils.capitalizeText(deliveryPerson);
        }
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (deliveryTime != null) {
            this.deliveryTime = deliveryTime;
        }
    }

    public static Delivery createDelivery(Order order, String deliveryPerson, LocalDateTime deliveryTime) {
        if (order != null && DeliveryUtils.isValidName(deliveryPerson) && deliveryTime != null) {
            return new Delivery(order, deliveryPerson, deliveryTime);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "order=" + order +
                ", deliveryPerson='" + deliveryPerson + '\'' +
                ", deliveryTime=" + deliveryTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delivery delivery)) return false;
        return Objects.equals(order, delivery.order) &&
                Objects.equals(deliveryPerson, delivery.deliveryPerson) &&
                Objects.equals(deliveryTime, delivery.deliveryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, deliveryPerson, deliveryTime);
    }
}
