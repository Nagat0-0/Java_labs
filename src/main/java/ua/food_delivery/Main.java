package ua.food_delivery;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.*;
import ua.food_delivery.repository.CustomerRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Lab 8: Validation Demo ===\n");

        demonstrateRecordValidation();
        System.out.println("\n" + "-".repeat(50) + "\n");
        demonstrateClassSetterValidation();

        System.out.println("\nDONE!!!");
    }

    private static void demonstrateRecordValidation() {
        System.out.println("--- 1. Record Validation (Customer) ---");

        try {
            System.out.println("Attempting to create VALID Customer...");
            Customer valid = Customer.createCustomer("Ivan", "Franko", "Lviv St. 10");
            System.out.println("SUCCESS: " + valid);

            CustomerRepository repo = new CustomerRepository();
            repo.add(valid);
            System.out.println("Added to repository.");
        } catch (InvalidDataException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nAttempting to create INVALID Customer (Empty Name, Bad Pattern)...");
            Customer invalid = Customer.createCustomer("123", "", "Short");
        } catch (InvalidDataException e) {
            System.err.println("CAUGHT EXPECTED EXCEPTION:\n" + e.getMessage());
        }
    }

    private static void demonstrateClassSetterValidation() {
        System.out.println("--- 2. Class Setter Validation (Restaurant) ---");

        Restaurant restaurant = null;
        try {
            restaurant = Restaurant.createRestaurant("Valid Place", CuisineType.ITALIAN, "Kyiv Center");
            System.out.println("Created: " + restaurant);
        } catch (InvalidDataException e) { return; }

        System.out.println("\nCurrent Name: " + restaurant.getName());
        try {
            System.out.println("Attempting to set INVALID name (empty)...");
            restaurant.setName("");
        } catch (InvalidDataException e) {
            System.err.println("Setter Error: " + e.getMessage());
        }

        System.out.println("Name after failed setter: " + restaurant.getName() + " (Should remain 'Valid Place')");
    }
}