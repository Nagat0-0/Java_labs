package ua.food_delivery.service.loading;

import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.model.*;
import ua.food_delivery.repository.*;
import ua.food_delivery.service.LoadResult;
import ua.food_delivery.util.LoggerUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ExecutorLoadingStrategy implements LoadingStrategy {

    private static final Logger logger = LoggerUtil.getLogger();
    private final int threadPoolSize;

    public ExecutorLoadingStrategy(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public ExecutorLoadingStrategy() {
        this(4);
    }

    @Override
    public LoadResult load(
            CustomerRepository customerRepo,
            RestaurantRepository restaurantRepo,
            MenuItemRepository menuItemRepo,
            OrderRepository orderRepo,
            DataLoader dataLoader) {

        logger.info("Starting loading with ExecutorService (pool size: " + threadPoolSize + ")...");
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        try {
            CompletableFuture<Integer> customersFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, Customer.class, customerRepo), executor);

            CompletableFuture<Integer> restaurantsFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, Restaurant.class, restaurantRepo), executor);

            CompletableFuture<Integer> menuItemsFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, MenuItem.class, menuItemRepo), executor);

            CompletableFuture<Integer> ordersFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, Order.class, orderRepo), executor);

            CompletableFuture.allOf(customersFuture, restaurantsFuture, menuItemsFuture, ordersFuture).join();

            long duration = System.currentTimeMillis() - startTime;
            logger.info("ExecutorService loading completed in " + duration + " ms");

            return new LoadResult(
                    customersFuture.join(),
                    restaurantsFuture.join(),
                    menuItemsFuture.join(),
                    ordersFuture.join(),
                    duration
            );
        } finally {
            shutdownExecutor(executor);
        }
    }

    private <T> int loadEntity(DataLoader dataLoader, Class<T> clazz, GenericRepository<T> repository) {
        try {
            return dataLoader.loadEntity(clazz, repository);
        } catch (DataSerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}