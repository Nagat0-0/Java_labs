package ua.food_delivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.food_delivery.util.DeliveryUtils;
import ua.food_delivery.util.ValidationUtils;

import java.time.LocalDateTime;

public record Delivery(
        @NotNull(message = "Order cannot be null")
        Order order,

        @NotBlank(message = "Delivery person name cannot be empty")
        String deliveryPerson,

        @NotNull(message = "Delivery time cannot be null")
        @FutureOrPresent(message = "Delivery time cannot be in the past")
        LocalDateTime deliveryTime
) {
    private static final Logger logger = LoggerFactory.getLogger(Delivery.class);

    public Delivery {
        deliveryPerson = DeliveryUtils.capitalizeText(deliveryPerson);
        logger.info("Delivery record initialized for person: {}", deliveryPerson);
    }

    public static Delivery createDelivery(Order order, String deliveryPerson, LocalDateTime deliveryTime) {
        Delivery delivery = new Delivery(order, deliveryPerson, deliveryTime);
        ValidationUtils.validate(delivery);
        return delivery;
    }
}