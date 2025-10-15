package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.DeliveryUtils;
import java.time.LocalDateTime;

public record Delivery(Order order, String deliveryPerson, LocalDateTime deliveryTime) {

    public Delivery {
        if (order == null) {
            throw new InvalidDataException("Order cannot be null");
        }
        if (!DeliveryUtils.isValidName(deliveryPerson)) {
            throw new InvalidDataException("Invalid delivery person name: " + deliveryPerson);
        }
        if (deliveryTime == null) {
            throw new InvalidDataException("Delivery time cannot be null");
        }

        deliveryPerson = DeliveryUtils.capitalizeText(deliveryPerson);
    }

    public static Delivery createDelivery(Order order, String deliveryPerson, LocalDateTime deliveryTime)
            throws InvalidDataException {
        return new Delivery(order, deliveryPerson, deliveryTime);
    }
}
