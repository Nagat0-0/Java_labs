package ua.food_delivery.service.loading;

import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.model.*;
import ua.food_delivery.repository.*;
import ua.food_delivery.service.LoadResult;
import ua.food_delivery.util.LoggerUtil;

import java.util.logging.Logger;

public class SequentialLoadingStrategy implements LoadingStrategy {

    private static final Logger logger = LoggerUtil.getLogger();

    @Override
    public LoadResult load(
            CustomerRepository customerRepo,
            RestaurantRepository restaurantRepo,
            MenuItemRepository menuItemRepo,
            OrderRepository orderRepo,
            DataLoader dataLoader) {

        logger.info("Starting sequential loading...");
        long startTime = System.currentTimeMillis();

        try {
            int customers = dataLoader.loadEntity(Customer.class, customerRepo);
            int restaurants = dataLoader.loadEntity(Restaurant.class, restaurantRepo);
            int menuItems = dataLoader.loadEntity(MenuItem.class, menuItemRepo);
            int orders = dataLoader.loadEntity(Order.class, orderRepo);

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Sequential loading completed in " + duration + " ms");

            return new LoadResult(customers, restaurants, menuItems, orders, duration);
        } catch (DataSerializationException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.severe("Sequential loading failed after " + duration + " ms: " + e.getMessage());
            throw new RuntimeException("Failed to load data", e);
        }
    }
}