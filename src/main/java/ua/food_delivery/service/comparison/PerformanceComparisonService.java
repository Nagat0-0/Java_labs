package ua.food_delivery.service.comparison;

import ua.food_delivery.model.MenuItem;
import ua.food_delivery.model.Restaurant;
import ua.food_delivery.model.CuisineType;
import ua.food_delivery.repository.MenuItemRepository;
import ua.food_delivery.repository.RestaurantRepository;
import ua.food_delivery.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PerformanceComparisonService {

    private static final Logger logger = LoggerUtil.getLogger();

    public ComparisonResult compareMenuItemFiltering(MenuItemRepository repository, String namePart) {
        logger.info("Comparing MenuItem filtering approaches for name containing: '" + namePart + "'");

        long startSequential = System.currentTimeMillis();
        List<MenuItem> sequentialResult = repository.getAll().stream()
                .filter(m -> m.getName().toLowerCase().contains(namePart.toLowerCase()))
                .toList();
        long sequentialTime = System.currentTimeMillis() - startSequential;

        long startParallel = System.currentTimeMillis();
        List<MenuItem> parallelResult = repository.getAll().parallelStream()
                .filter(m -> m.getName().toLowerCase().contains(namePart.toLowerCase()))
                .toList();
        long parallelTime = System.currentTimeMillis() - startParallel;

        long startExecutor = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            List<MenuItem> allItems = repository.getAll();
            int chunkSize = Math.max(1, allItems.size() / 4);
            List<CompletableFuture<List<MenuItem>>> futures = new ArrayList<>();

            for (int i = 0; i < allItems.size(); i += chunkSize) {
                int start = i;
                int end = Math.min(i + chunkSize, allItems.size());
                List<MenuItem> chunk = allItems.subList(start, end);

                CompletableFuture<List<MenuItem>> future = CompletableFuture.supplyAsync(() ->
                        chunk.stream()
                                .filter(m -> m.getName().toLowerCase().contains(namePart.toLowerCase()))
                                .toList(), executor);
                futures.add(future);
            }

            List<MenuItem> executorResult = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .toList();
        } finally {
            executor.shutdown();
        }
        long executorTime = System.currentTimeMillis() - startExecutor;

        ComparisonResult result = new ComparisonResult(
                "MenuItem Filtering by Name",
                sequentialResult.size(),
                sequentialTime,
                parallelTime,
                executorTime
        );

        logger.info("Comparison completed: " + result);
        return result;
    }

    public ComparisonResult compareRestaurantCounting(RestaurantRepository repository) {
        logger.info("Comparing restaurant counting approaches");

        long startSequential = System.currentTimeMillis();
        Map<CuisineType, Long> sequentialResult = repository.getAll().stream()
                .collect(Collectors.groupingBy(
                        Restaurant::getCuisineType,
                        Collectors.counting()
                ));
        long sequentialTime = System.currentTimeMillis() - startSequential;

        long startParallel = System.currentTimeMillis();
        Map<CuisineType, Long> parallelResult = repository.getAll().parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        Restaurant::getCuisineType,
                        Collectors.counting()
                ));
        long parallelTime = System.currentTimeMillis() - startParallel;

        long startExecutor = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            List<Restaurant> allRestaurants = repository.getAll();

            Callable<Long> countItalian = () -> allRestaurants.stream()
                    .filter(r -> r.getCuisineType() == CuisineType.ITALIAN).count();

            Callable<Long> countJapanese = () -> allRestaurants.stream()
                    .filter(r -> r.getCuisineType() == CuisineType.JAPANESE).count();

            Callable<Long> countAmerican = () -> allRestaurants.stream()
                    .filter(r -> r.getCuisineType() == CuisineType.AMERICAN).count();

            Future<Long> italianFuture = executor.submit(countItalian);
            Future<Long> japaneseFuture = executor.submit(countJapanese);
            Future<Long> americanFuture = executor.submit(countAmerican);

            italianFuture.get();
            japaneseFuture.get();
            americanFuture.get();

        } catch (InterruptedException | ExecutionException e) {
            logger.severe("Error in executor comparison: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
        long executorTime = System.currentTimeMillis() - startExecutor;

        ComparisonResult result = new ComparisonResult(
                "Restaurant Counting by Cuisine",
                sequentialResult.size(),
                sequentialTime,
                parallelTime,
                executorTime
        );

        logger.info("Comparison completed: " + result);
        return result;
    }

    public ComparisonResult compareMenuItemGroupingWithDelay(MenuItemRepository repository) {
        logger.info("Comparing MenuItem grouping by Price Category (Slow op)");

        long startSequential = System.currentTimeMillis();
        Map<String, List<MenuItem>> sequentialResult = repository.getAll().stream()
                .collect(Collectors.groupingBy(MenuItem::getPriceCategory));
        long sequentialTime = System.currentTimeMillis() - startSequential;

        long startParallel = System.currentTimeMillis();
        Map<String, List<MenuItem>> parallelResult = repository.getAll().parallelStream()
                .collect(Collectors.groupingByConcurrent(MenuItem::getPriceCategory));
        long parallelTime = System.currentTimeMillis() - startParallel;

        long startExecutor = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            List<MenuItem> allItems = repository.getAll();

            Callable<List<MenuItem>> budgetTask = () -> allItems.stream()
                    .filter(m -> "Budget".equals(m.getPriceCategory())).toList();

            Callable<List<MenuItem>> standardTask = () -> allItems.stream()
                    .filter(m -> "Standard".equals(m.getPriceCategory())).toList();

            Callable<List<MenuItem>> premiumTask = () -> allItems.stream()
                    .filter(m -> "Premium".equals(m.getPriceCategory())).toList();

            Future<List<MenuItem>> f1 = executor.submit(budgetTask);
            Future<List<MenuItem>> f2 = executor.submit(standardTask);
            Future<List<MenuItem>> f3 = executor.submit(premiumTask);

            f1.get(); f2.get(); f3.get();

        } catch (InterruptedException | ExecutionException e) {
            logger.severe("Error in executor: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
        long executorTime = System.currentTimeMillis() - startExecutor;

        return new ComparisonResult(
                "Group MenuItems by PriceCategory (Simulated Delay)",
                sequentialResult.size(),
                sequentialTime,
                parallelTime,
                executorTime
        );
    }
}