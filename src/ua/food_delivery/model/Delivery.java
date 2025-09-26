package ua.food_delivery.model;

import ua.food_delivery.util.DeliveryUtils;

import java.time.LocalDateTime;

public record Delivery(Order order, String deliveryPerson, LocalDateTime deliveryTime) {

    public Delivery {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (!DeliveryUtils.isValidName(deliveryPerson)) {
            throw new IllegalArgumentException("Invalid delivery person name");
        }
        if (deliveryTime == null) {
            throw new IllegalArgumentException("Delivery time cannot be null");
        }

        deliveryPerson = DeliveryUtils.capitalizeText(deliveryPerson);
    }
}
