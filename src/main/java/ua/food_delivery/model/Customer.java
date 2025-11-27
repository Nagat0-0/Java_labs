package ua.food_delivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.food_delivery.util.CustomerUtils;
import ua.food_delivery.util.ValidationUtils;

import java.util.Comparator;

public record Customer(
        @NotBlank(message = "First name cannot be empty")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 chars")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters")
        String firstName,

        @NotBlank(message = "Last name cannot be empty")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 chars")
        String lastName,

        @NotBlank(message = "Address cannot be empty")
        @Size(min = 5, max = 100, message = "Address must be between 5 and 100 chars")
        String address
) implements Comparable<Customer> {

    private static final Logger logger = LoggerFactory.getLogger(Customer.class);

    public Customer {
        firstName = CustomerUtils.capitalizeText(firstName);
        lastName = CustomerUtils.capitalizeText(lastName);
        address = address != null ? address.trim() : null;

        logger.info("Customer record initialized: {} {}", firstName, lastName);
    }

    public static Customer createCustomer(String firstName, String lastName, String address) {
        Customer customer = new Customer(firstName, lastName, address);
        ValidationUtils.validate(customer);
        return customer;
    }

    @Override
    public int compareTo(Customer o) {
        return Comparator.comparing(Customer::lastName)
                .thenComparing(Customer::firstName)
                .compare(this, o);
    }
}