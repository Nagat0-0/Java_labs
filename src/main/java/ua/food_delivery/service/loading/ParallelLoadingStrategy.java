package ua.food_delivery.service.loading;

import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.model.*;
import ua.food_delivery.repository.*;
import ua.food_delivery.service.LoadResult;
import ua.food_delivery.util.LoggerUtil;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ParallelLoadingStrategy implements LoadingStrategy {

    private static final Logger logger = LoggerUtil.getLogger();

    @Override
    public LoadResult load(
            CustomerRepository customerRepo,
            RestaurantRepository restaurantRepo,
            MenuItemRepository menuItemRepo,
            OrderRepository orderRepo,
            DataLoader dataLoader) {

        logger.info("Starting parallel loading with CompletableFuture...");
        long startTime = System.currentTimeMillis();

        CompletableFuture<Integer> customersFuture = CompletableFuture
                .supplyAsync(() -> loadEntity(dataLoader, Customer.class, customerRepo))
                .exceptionally(ex -> handleError("customers", ex));

        CompletableFuture<Integer> restaurantsFuture = CompletableFuture
                .supplyAsync(() -> loadEntity(dataLoader, Restaurant.class, restaurantRepo))
                .exceptionally(ex -> handleError("restaurants", ex));

        CompletableFuture<Integer> menuItemsFuture = CompletableFuture
                .supplyAsync(() -> loadEntity(dataLoader, MenuItem.class, menuItemRepo))
                .exceptionally(ex -> handleError("menuItems", ex));

        CompletableFuture<Integer> ordersFuture = CompletableFuture
                .supplyAsync(() -> loadEntity(dataLoader, Order.class, orderRepo))
                .exceptionally(ex -> handleError("orders", ex));

        CompletableFuture.allOf(customersFuture, restaurantsFuture, menuItemsFuture, ordersFuture).join();

        long duration = System.currentTimeMillis() - startTime;
        logger.info("Parallel loading completed in " + duration + " ms");

        return new LoadResult(
                customersFuture.join(),
                restaurantsFuture.join(),
                menuItemsFuture.join(),
                ordersFuture.join(),
                duration
        );
    }

    private <T> int loadEntity(DataLoader dataLoader, Class<T> clazz, GenericRepository<T> repository) {
        try {
            return dataLoader.loadEntity(clazz, repository);
        } catch (DataSerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private int handleError(String entityType, Throwable ex) {
        logger.severe("Failed to load " + entityType + ": " + ex.getMessage());
        return 0;
    }
}