package ua.food_delivery.util;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.*;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class FileReaderUtil {
    private static final Logger logger = LoggerUtil.getLogger();

    // ---------- Restaurant ----------
    public static List<Restaurant> readRestaurantsFromFile(String filePath)
            throws InvalidDataException {
        List<Restaurant> restaurants = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new InvalidDataException("Invalid format at line " + lineNumber);
                }

                try {
                    String name = parts[0].trim();
                    CuisineType cuisine = CuisineType.valueOf(parts[1].trim().toUpperCase());
                    String location = parts[2].trim();

                    Restaurant restaurant = Restaurant.createRestaurant(name, cuisine, location);
                    restaurants.add(restaurant);
                    logger.info("Restaurant created: " + restaurant);
                } catch (InvalidDataException e) {
                    logger.warning("Invalid data at line " + lineNumber + ": " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    logger.warning("Unknown cuisine type at line " + lineNumber);
                }
            }

        } catch (FileNotFoundException e) {
            throw new InvalidDataException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new InvalidDataException("Error reading file: " + filePath, e);
        } finally {
            logger.info("Finished reading restaurants file.");
        }

        return restaurants;
    }

    // ---------- Customer ----------
    public static List<Customer> readCustomersFromFile(String filePath)
            throws InvalidDataException {
        List<Customer> customers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new InvalidDataException("Invalid format at line " + lineNumber);
                }

                try {
                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String address = parts[2].trim();

                    customers.add(new Customer(firstName, lastName, address));
                    logger.info("Customer created: " + firstName + " " + lastName);
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid customer data at line " + lineNumber + ": " + e.getMessage());
                } catch (InvalidDataException e) {
                    logger.warning("Invalid customer data at line " + lineNumber + ": " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            throw new InvalidDataException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new InvalidDataException("Error reading file: " + filePath, e);
        } finally {
            logger.info("Finished reading customers file.");
        }

        return customers;
    }

    // ---------- MenuItem ----------
    public static List<MenuItem> readMenuItemsFromFile(String filePath)
            throws InvalidDataException {
        List<MenuItem> items = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new InvalidDataException("Invalid format at line " + lineNumber);
                }

                try {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    String category = parts[2].trim();

                    MenuItem item = MenuItem.createMenuItem(name, price, category);
                    items.add(item);
                    logger.info("Menu item created: " + item);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid price format at line " + lineNumber);
                } catch (InvalidDataException e) {
                    logger.warning("Invalid menu item data at line " + lineNumber + ": " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            throw new InvalidDataException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new InvalidDataException("Error reading file: " + filePath, e);
        } finally {
            logger.info("Finished reading menu items file.");
        }

        return items;
    }
}
