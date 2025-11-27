package ua.food_delivery;

import ua.food_delivery.config.AppConfig;
import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.model.*;
import ua.food_delivery.persistence.PersistenceManager;
import ua.food_delivery.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Lab 7: Persistence Demo (Full) ===\n");

        try {
            AppConfig config = new AppConfig();
            PersistenceManager manager = new PersistenceManager(config);

            demonstrateCustomerSerialization(config, manager);
            System.out.println("\n" + "=".repeat(70) + "\n");

            demonstrateRestaurantSerialization(config, manager);
            System.out.println("\n" + "=".repeat(70) + "\n");

            demonstrateMenuItemSerialization(config, manager);
            System.out.println("\n" + "=".repeat(70) + "\n");

            demonstrateOrderSerialization(config, manager);

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR IN MAIN:");
            e.printStackTrace();
        }
        System.out.println("\nDONE!!!");
    }

    private static void demonstrateCustomerSerialization(AppConfig config, PersistenceManager manager)
            throws DataSerializationException {
        System.out.println("### CUSTOMER SERIALIZATION (JSON) ###");
        List<Customer> repo = new ArrayList<>();
        repo.add(Customer.createCustomer("Ivan", "Zebra", "Kyiv, Main St. 1"));
        repo.add(Customer.createCustomer("Anna", "Apple", "Lviv, Rynok Sq. 10"));

        manager.save(repo, "customers", Customer.class, "JSON");
        System.out.println("Saved to: " + config.getJsonFilePath("customers"));

        List<Customer> loaded = manager.load("customers", Customer.class, "JSON");
        System.out.println("Loaded " + loaded.size() + " items.");
        loaded.forEach(System.out::println);
    }

    private static void demonstrateRestaurantSerialization(AppConfig config, PersistenceManager manager)
            throws DataSerializationException {
        System.out.println("### RESTAURANT SERIALIZATION (YAML) ###");
        List<Restaurant> repo = new ArrayList<>();
        repo.add(Restaurant.createRestaurant("Sushi Master", CuisineType.JAPANESE, "Kyiv"));
        repo.add(Restaurant.createRestaurant("Pasta House", CuisineType.ITALIAN, "Lviv"));

        manager.save(repo, "restaurants", Restaurant.class, "YAML");
        System.out.println("Saved to: " + config.getYamlFilePath("restaurants"));

        List<Restaurant> loaded = manager.load("restaurants", Restaurant.class, "YAML");
        System.out.println("Loaded " + loaded.size() + " items.");
        loaded.forEach(System.out::println);
    }

    private static void demonstrateMenuItemSerialization(AppConfig config, PersistenceManager manager)
            throws DataSerializationException {
        System.out.println("### MENU ITEM SERIALIZATION (JSON) ###");
        List<MenuItem> repo = new ArrayList<>();
        repo.add(MenuItem.createMenuItem("Margherita", 150.0, "Pizza"));
        repo.add(MenuItem.createMenuItem("Cola", 40.0, "Drinks"));
        repo.add(MenuItem.createMenuItem("Cheesecake", 90.0, "Dessert"));

        manager.save(repo, "menuitems", MenuItem.class, "JSON");
        System.out.println("Saved to: " + config.getJsonFilePath("menuitems"));

        List<MenuItem> loaded = manager.load("menuitems", MenuItem.class, "JSON");
        System.out.println("Loaded " + loaded.size() + " items.");
        loaded.forEach(System.out::println);
    }

    private static void demonstrateOrderSerialization(AppConfig config, PersistenceManager manager)
            throws DataSerializationException {
        System.out.println("### ORDER SERIALIZATION (YAML - COMPLEX OBJECTS) ###");

        Customer c = Customer.createCustomer("Test", "User", "Order St. 5");
        MenuItem m1 = MenuItem.createMenuItem("Burger", 200.0, "Main");
        MenuItem m2 = MenuItem.createMenuItem("Fries", 60.0, "Side");

        List<Order> repo = new ArrayList<>();
        repo.add(Order.createOrder(c, List.of(m1, m2), LocalDateTime.now(), OrderStatus.PENDING));
        repo.add(Order.createOrder(c, List.of(m1), LocalDateTime.now().minusHours(2), OrderStatus.DELIVERED));

        manager.save(repo, "orders", Order.class, "YAML");
        System.out.println("Saved to: " + config.getYamlFilePath("orders"));

        List<Order> loaded = manager.load("orders", Order.class, "YAML");
        System.out.println("Loaded " + loaded.size() + " orders.");


        for (Order o : loaded) {
            System.out.println("Order Status: " + o.getStatus() + ", Items count: " + o.getItems().size());
            System.out.println(" -> First item: " + o.getItems().get(0).getName());
        }
    }
}