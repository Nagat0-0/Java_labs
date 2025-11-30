package ua.food_delivery;

import ua.food_delivery.config.AppConfig;
import ua.food_delivery.model.MenuItem;
import ua.food_delivery.persistence.PersistenceManager;
import ua.food_delivery.repository.*;
import ua.food_delivery.service.LoadResult;
import ua.food_delivery.service.comparison.ComparisonResult;
import ua.food_delivery.service.comparison.PerformanceComparisonService;
import ua.food_delivery.service.loading.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        PersistenceManager persistenceManager = new PersistenceManager(config);

        CustomerRepository customerRepository = new CustomerRepository();
        RestaurantRepository restaurantRepository = new RestaurantRepository();
        MenuItemRepository menuItemRepository = new MenuItemRepository();
        OrderRepository orderRepository = new OrderRepository();

        demonstrateParallelLoading(
                persistenceManager,
                customerRepository,
                restaurantRepository,
                menuItemRepository,
                orderRepository
        );

        prepareBulkData(menuItemRepository);

        demonstratePerformanceComparison(
                menuItemRepository,
                restaurantRepository
        );

        System.out.println("\nDONE!!!");
    }

    private static void demonstrateParallelLoading(
            PersistenceManager persistenceManager,
            CustomerRepository customerRepository,
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository,
            OrderRepository orderRepository) {

        DataLoader dataLoader = new DataLoader(persistenceManager);

        LoadResult sequentialResult = dataLoader.load(
                customerRepository,
                restaurantRepository,
                menuItemRepository,
                orderRepository,
                new SequentialLoadingStrategy()
        );
        System.out.println(sequentialResult);

        clearRepositories(customerRepository, restaurantRepository, menuItemRepository, orderRepository);

        LoadResult parallelResult = dataLoader.load(
                customerRepository,
                restaurantRepository,
                menuItemRepository,
                orderRepository,
                new ParallelLoadingStrategy()
        );
        System.out.println(parallelResult);

        clearRepositories(customerRepository, restaurantRepository, menuItemRepository, orderRepository);

        LoadResult executorResult = dataLoader.load(
                customerRepository,
                restaurantRepository,
                menuItemRepository,
                orderRepository,
                new ExecutorLoadingStrategy(4)
        );
        System.out.println(executorResult);
    }

    private static void demonstratePerformanceComparison(
            MenuItemRepository menuItemRepo,
            RestaurantRepository restaurantRepo) {

        PerformanceComparisonService comparisonService = new PerformanceComparisonService();

        ComparisonResult filterResult = comparisonService.compareMenuItemFiltering(
                menuItemRepo, "Item"
        );
        System.out.println(filterResult);

        ComparisonResult countResult = comparisonService.compareRestaurantCounting(restaurantRepo);
        System.out.println(countResult);

        ComparisonResult groupResult = comparisonService.compareMenuItemGroupingWithDelay(menuItemRepo);
        System.out.println(groupResult);
    }

    private static void clearRepositories(
            CustomerRepository c, RestaurantRepository r, MenuItemRepository m, OrderRepository o) {
        c.clear();
        r.clear();
        m.clear();
        o.clear();
    }

    private static void prepareBulkData(MenuItemRepository repo) {
        List<MenuItem> items = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            items.add(MenuItem.createMenuItem("Item " + i, 50.0 + (i % 400), "Bulk"));
        }
        repo.addAll(items);
    }
}