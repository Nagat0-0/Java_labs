package ua.food_delivery.model;

import ua.food_delivery.util.CustomerUtils;

public record Customer(String firstName, String lastName, String address) {

    public Customer {
        if (!CustomerUtils.isValidName(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        if (!CustomerUtils.isValidName(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        if (!CustomerUtils.isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address");
        }

        firstName = CustomerUtils.capitalizeText(firstName);
        lastName = CustomerUtils.capitalizeText(lastName);
        address = address.trim();
    }
}
