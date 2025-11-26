package ua.food_delivery;

import ua.food_delivery.model.*;
import ua.food_delivery.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Lab 6: Stream API Demo ===\n");

        try {
            demonstrateStreamMethods();
            System.out.println("\n--------------------------------------------------");
            demonstrateParallelStreamPerformance();
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR:");
            e.printStackTrace();
        }

        System.out.println("\nDONE!!!");
    }

    private static void demonstrateStreamMethods() {
        System.out.println("--- Stream API Methods ---");

        CustomerRepository customerRepo = new CustomerRepository();
        customerRepo.add(Customer.createCustomer("Ivan", "Petrenko", "Kyiv, Main St. 1"));
        customerRepo.add(Customer.createCustomer("Oksana", "Shevchenko", "Lviv, Rynok Sq. 10"));

        System.out.println("Customers in Kyiv:");
        customerRepo.findByAddressContaining("Kyiv").forEach(System.out::println);

        RestaurantRepository restRepo = new RestaurantRepository();
        restRepo.add(Restaurant.createRestaurant("A", CuisineType.ITALIAN, "Loc1"));
        restRepo.add(Restaurant.createRestaurant("B", CuisineType.ITALIAN, "Loc2"));
        restRepo.add(Restaurant.createRestaurant("C", CuisineType.JAPANESE, "Loc3"));

        System.out.println("\nRestaurants grouped by Cuisine:");
        restRepo.groupByCuisine().forEach((cuisine, list) ->
                System.out.println(cuisine + ": " + list.size() + " restaurants")
        );

        OrderRepository orderRepo = new OrderRepository();
        Customer c = Customer.createCustomer("User", "Test", "Addr 111");
        MenuItem m1 = MenuItem.createMenuItem("Pizza", 200, "Food");
        MenuItem m2 = MenuItem.createMenuItem("Cola", 50, "Drink");

        orderRepo.add(Order.createOrder(c, List.of(m1, m2), LocalDateTime.now(), OrderStatus.DELIVERED));
        orderRepo.add(Order.createOrder(c, List.of(m1), LocalDateTime.now(), OrderStatus.PENDING));

        System.out.println("\nTotal Revenue: " + orderRepo.calculateTotalRevenue() + " UAH (Expected: 450.0)");
    }

    private static void demonstrateParallelStreamPerformance() {
        System.out.println("--- Parallel Stream Performance Test ---");

        List<Double> data = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 1_000_000; i++) {
            data.add(rand.nextDouble() * 100);
        }
        System.out.println("Dataset size: " + data.size());

        long startSeq = System.nanoTime();
        long countSeq = data.stream()
                .filter(d -> d > 50.0)
                .sorted()
                .count();
        long endSeq = System.nanoTime();
        System.out.println("Sequential Stream: " + (endSeq - startSeq) / 1_000_000 + " ms");

        long startPar = System.nanoTime();
        long countPar = data.parallelStream()
                .filter(d -> d > 50.0)
                .sorted()
                .count();
        long endPar = System.nanoTime();
        System.out.println("Parallel Stream:   " + (endPar - startPar) / 1_000_000 + " ms");
    }
}