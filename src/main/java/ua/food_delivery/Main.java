package ua.food_delivery;

import ua.food_delivery.model.*;
import ua.food_delivery.repository.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Lab 5: Sorting & Comparators Demo ===\n");

        try {
            demonstrateCustomerSorting();
            System.out.println("\n--------------------------------------------------");
            demonstrateRestaurantSorting();
            System.out.println("\n--------------------------------------------------");
            demonstrateMenuItemSorting();
            System.out.println("\n--------------------------------------------------");
            demonstrateOrderSorting();
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR IN MAIN:");
            e.printStackTrace();
        }

        System.out.println("\nDONE!!!");
    }

    private static void demonstrateCustomerSorting() {
        System.out.println("--- Customer Repository Sorting ---");
        CustomerRepository repo = new CustomerRepository();

        // Виправлено адреси на довші, щоб пройти валідацію (min 5 символів)
        repo.add(Customer.createCustomer("Ivan", "Zebra", "Kyiv, Main St. 1"));
        repo.add(Customer.createCustomer("Anna", "Apple", "Lviv, Long Street Name 55"));
        repo.add(Customer.createCustomer("Petro", "Borets", "Odesa, Sea St. 2"));

        System.out.println("\n1. Internal Sort by Identity (Name+Surname) ASC:");
        repo.sortByIdentity("asc");
        repo.getAll().forEach(System.out::println);

        System.out.println("\n2. External Sort by Last Name (Natural Order):");
        repo.sortByName().forEach(System.out::println);

        System.out.println("\n3. External Sort by Address Length:");
        repo.sortByAddressLength().forEach(c -> System.out.println(c.firstName() + ": " + c.address()));
    }

    private static void demonstrateRestaurantSorting() {
        System.out.println("\n--- Restaurant Repository Sorting ---");
        RestaurantRepository repo = new RestaurantRepository();

        repo.add(Restaurant.createRestaurant("Sushi Master", CuisineType.JAPANESE, "Kyiv, Center"));
        repo.add(Restaurant.createRestaurant("Pasta House", CuisineType.ITALIAN, "Lviv, Rynok"));
        repo.add(Restaurant.createRestaurant("Burger King", CuisineType.AMERICAN, "Zhitomir, Mall"));
        repo.add(Restaurant.createRestaurant("Alfredo", CuisineType.ITALIAN, "Kyiv, Left Bank"));

        System.out.println("\n1. Internal Sort by Identity (Name) DESC:");
        repo.sortByIdentity("desc");
        repo.getAll().forEach(r -> System.out.println(r.getName()));

        System.out.println("\n2. External Sort by Cuisine then Name:");
        repo.sortByCuisineAndName().forEach(r -> System.out.println(r.getCuisineType() + " - " + r.getName()));
    }

    private static void demonstrateMenuItemSorting() {
        System.out.println("\n--- MenuItem Repository Sorting ---");
        MenuItemRepository repo = new MenuItemRepository();

        repo.add(MenuItem.createMenuItem("Water", 20.0, "Drinks"));
        repo.add(MenuItem.createMenuItem("Steak", 500.0, "Main"));
        repo.add(MenuItem.createMenuItem("Soup", 150.0, "Starter"));
        repo.add(MenuItem.createMenuItem("Juice", 50.0, "Drinks"));

        System.out.println("\n1. External Sort by Price ASC:");
        repo.sortByPriceAsc().forEach(m -> System.out.println(m.getName() + ": " + m.getPrice()));
    }

    private static void demonstrateOrderSorting() {
        System.out.println("\n--- Order Repository Sorting ---");
        OrderRepository repo = new OrderRepository();

        Customer c = Customer.createCustomer("Test", "User", "Valid Address 123");
        MenuItem m1 = MenuItem.createMenuItem("Item A", 10, "Cat A");

        repo.add(Order.createOrder(c, List.of(m1), LocalDateTime.now().minusDays(1), OrderStatus.DELIVERED));
        repo.add(Order.createOrder(c, List.of(m1), LocalDateTime.now(), OrderStatus.PENDING));

        System.out.println("\n1. External Sort by Date DESC:");
        repo.sortByDateDesc().forEach(o -> System.out.println(o.getStatus() + " at " + o.getOrderDate()));
    }
}