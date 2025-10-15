package ua.food_delivery;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.*;
import ua.food_delivery.util.FileReaderUtil;
import ua.food_delivery.util.LoggerUtil;

import java.util.List;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        logger.info("=== Food Delivery App Started ===");

        try {
            String restaurantsFile = "src/data/java/ua/food_delivery/restaurants.csv";
            String customersFile = "src/data/java/ua/food_delivery/customers.csv";
            String menuFile = "src/data/java/ua/food_delivery/menuItems.csv";

            List<Restaurant> restaurants = FileReaderUtil.readRestaurantsFromFile(restaurantsFile);
            List<Customer> customers = FileReaderUtil.readCustomersFromFile(customersFile);
            List<MenuItem> menuItems = FileReaderUtil.readMenuItemsFromFile(menuFile);

            logger.info("Successfully read all data from files.");
            logger.info("Restaurants count: " + restaurants.size());
            logger.info("Customers count: " + customers.size());
            logger.info("Menu items count: " + menuItems.size());

            // Демонстрація створення об’єкта
            Restaurant r = restaurants.get(0);
            Customer c = customers.get(0);
            MenuItem m = menuItems.get(0);
            Order order = Order.createOrder(c, List.of(m), java.time.LocalDateTime.now(), OrderStatus.CONFIRMED);
            logger.info("Order created: " + order);

        } catch (InvalidDataException e) {
            logger.severe("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
        } finally {
            logger.info("=== Food Delivery App Finished ===");
        }
    }
}
