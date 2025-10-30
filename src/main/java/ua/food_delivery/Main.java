package ua.food_delivery;

import ua.food_delivery.model.*;
import ua.food_delivery.repository.GenericRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        System.out.println("--- Customer Repository Demonstration ---");
        demonstrateCustomerRepository();

        System.out.println("\n--- Restaurant Repository Demonstration ---");
        demonstrateRestaurantRepository();

        System.out.println("\n--- Menu Item Repository Demonstration ---");
        demonstrateMenuItemRepository();

        System.out.println("\n--- Order Repository Demonstration ---");
        demonstrateOrderRepository();

        System.out.println("\nDONE!!!");
    }

    private static void demonstrateCustomerRepository() {
        GenericRepository<Customer> customerRepo = new GenericRepository<>(
                customer -> customer.firstName() + " " + customer.lastName(),
                "Customer"
        );

        Customer c1 = Customer.createCustomer("Ivan", "Petrenko", "1 Khreshchatyk St.");
        Customer c2 = Customer.createCustomer("Maria", "Ivanenko", "10 Shevchenko St.");
        Customer c3_duplicate = Customer.createCustomer("Ivan", "Petrenko", "5 Kyivska St. (Different Address)");

        System.out.println("1. Adding Customers:");
        customerRepo.add(c1);
        customerRepo.add(c2);
        customerRepo.add(c3_duplicate);
        System.out.println("Total added (Expected 2): " + customerRepo.getAll().size());

        System.out.println("\n2. Finding by Identity (Maria Ivanenko):");
        customerRepo.findByIdentity("Maria Ivanenko")
                .ifPresentOrElse(
                        c -> System.out.println("Found: " + c),
                        () -> System.out.println("Not found")
                );

        System.out.println("\n3. All Customers:");
        customerRepo.getAll().forEach(c -> System.out.println("  - " + c));

        System.out.println("\n4. Deleting (Maria Ivanenko):");
        customerRepo.remove(c2);
        System.out.println("Customers left: " + customerRepo.getAll().size());
        customerRepo.findByIdentity("Maria Ivanenko")
                .ifPresentOrElse(
                        c -> System.out.println("Error: Customer still found!"),
                        () -> System.out.println("'Maria Ivanenko' successfully removed.")
                );
    }

    private static void demonstrateRestaurantRepository() {
        GenericRepository<Restaurant> restaurantRepo = new GenericRepository<>(
                Restaurant::getName,
                "Restaurant"
        );

        Restaurant r1 = Restaurant.createRestaurant("Pizza Palace", CuisineType.ITALIAN, "5 Main St.");
        Restaurant r2 = Restaurant.createRestaurant("Sushi World", CuisineType.JAPANESE, "20 Freedom Ave.");
        Restaurant r3_duplicate = Restaurant.createRestaurant("Pizza Palace", CuisineType.AMERICAN, "Different Address");

        System.out.println("1. Adding Restaurants:");
        restaurantRepo.add(r1);
        restaurantRepo.add(r2);
        restaurantRepo.add(r3_duplicate);
        System.out.println("Total added (Expected 2): " + restaurantRepo.getAll().size());

        System.out.println("\n2. Finding by Identity (Sushi World):");
        Optional<Restaurant> found = restaurantRepo.findByIdentity("Sushi World");
        found.ifPresentOrElse(
                r -> System.out.println("Found: " + r),
                () -> System.out.println("Not found")
        );

        System.out.println("\n3. Finding non-existent (McDonalds):");
        restaurantRepo.findByIdentity("McDonalds")
                .ifPresentOrElse(
                        r -> System.out.println("Found: " + r),
                        () -> System.out.println("Not found")
                );

        System.out.println("\n4. All Restaurants:");
        restaurantRepo.getAll().forEach(r -> System.out.println("  - " + r));

        System.out.println("\n5. Deleting (Pizza Palace):");
        restaurantRepo.remove(r1);
        System.out.println("Restaurants left: " + restaurantRepo.getAll().size());
    }

    private static void demonstrateMenuItemRepository() {
        GenericRepository<MenuItem> menuRepo = new GenericRepository<>(
                MenuItem::getName,
                "MenuItem"
        );

        MenuItem m1 = MenuItem.createMenuItem("Margherita Pizza", 150.0, "Pizza");
        MenuItem m2 = MenuItem.createMenuItem("Philadelphia Roll", 250.0, "Sushi");
        MenuItem m3_duplicate = MenuItem.createMenuItem("Margherita Pizza", 160.0, "Different Category");

        System.out.println("1. Adding Menu Items:");
        menuRepo.add(m1);
        menuRepo.add(m2);
        menuRepo.add(m3_duplicate);
        System.out.println("Total added (Expected 2): " + menuRepo.getAll().size());

        System.out.println("\n2. All Items:");
        menuRepo.getAll().forEach(m -> System.out.println("  - " + m));

        System.out.println("\n3. Finding (Philadelphia Roll):");
        menuRepo.findByIdentity("Philadelphia Roll")
                .ifPresent(m -> System.out.println("Found: " + m.getName() + ", Price: " + m.getPrice() + " UAH"));
    }

    private static void demonstrateOrderRepository() {
        GenericRepository<Order> orderRepo = new GenericRepository<>(
                order -> order.getCustomer().firstName() + "|" + order.getOrderDate().toString(),
                "Order"
        );

        Customer c1 = Customer.createCustomer("Anna", "Boiko", "100 Peace Ave.");
        MenuItem m1 = MenuItem.createMenuItem("Coffee", 40.0, "Drinks");
        MenuItem m2 = MenuItem.createMenuItem("Croissant", 60.0, "Pastry");
        LocalDateTime time1 = LocalDateTime.of(2025, 10, 20, 10, 30);
        LocalDateTime time2 = LocalDateTime.of(2025, 10, 20, 10, 45);

        Order o1 = Order.createOrder(c1, List.of(m1, m2), time1, OrderStatus.PENDING);
        Order o2 = Order.createOrder(c1, List.of(m1), time2, OrderStatus.PENDING);
        Order o3_duplicate = Order.createOrder(c1, List.of(m2), time1, OrderStatus.CONFIRMED);

        System.out.println("1. Adding Orders:");
        orderRepo.add(o1);
        orderRepo.add(o2);
        orderRepo.add(o3_duplicate);
        System.out.println("Total added (Expected 2): " + orderRepo.getAll().size());

        System.out.println("\n2. All Orders:");
        orderRepo.getAll().forEach(o -> System.out.println("  - " + o.getCustomer().firstName() + " at " + o.getOrderDate()));

        System.out.println("\n3. Finding Order (Anna|" + time1 + "):");
        String searchKey = "Anna|" + time1.toString();
        orderRepo.findByIdentity(searchKey)
                .ifPresent(o -> System.out.println("Found order with " + o.getItems().size() + " items"));

        System.out.println("\n4. Deleting Order:");
        orderRepo.remove(o1);
        System.out.println("Orders left: " + orderRepo.getAll().size());
    }
}