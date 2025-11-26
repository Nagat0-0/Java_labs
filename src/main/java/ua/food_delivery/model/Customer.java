package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.CustomerUtils;
import java.util.Comparator;

public record Customer(String firstName, String lastName, String address) implements Comparable<Customer> {

    public static final Comparator<Customer> NATURAL_ORDER =
            Comparator.comparing(Customer::lastName)
                    .thenComparing(Customer::firstName);

    public Customer {
        if (!CustomerUtils.isValidName(firstName)) {
            throw new InvalidDataException("Invalid first name: " + firstName);
        }
        if (!CustomerUtils.isValidName(lastName)) {
            throw new InvalidDataException("Invalid last name: " + lastName);
        }
        if (!CustomerUtils.isValidAddress(address)) {
            throw new InvalidDataException("Invalid address: " + address);
        }

        firstName = CustomerUtils.capitalizeText(firstName);
        lastName = CustomerUtils.capitalizeText(lastName);
        address = address.trim();
    }

    public static Customer createCustomer(String firstName, String lastName, String address) throws InvalidDataException {
        return new Customer(firstName, lastName, address);
    }

    @Override
    public int compareTo(Customer other) {
        return NATURAL_ORDER.compare(this, other);
    }
}