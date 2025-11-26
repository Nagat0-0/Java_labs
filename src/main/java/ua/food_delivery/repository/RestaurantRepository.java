package ua.food_delivery.repository;

import ua.food_delivery.model.Restaurant;
import ua.food_delivery.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class RestaurantRepository extends GenericRepository<Restaurant> {
    private static final Logger logger = LoggerUtil.getLogger();

    public RestaurantRepository() {
        super(Restaurant::getName, "Restaurant");
    }

    public List<Restaurant> sortByCuisineAndName() {
        List<Restaurant> all = getAll();
        all.sort(Comparator.comparing(Restaurant::getCuisineType)
                .thenComparing(Restaurant::getName));
        logger.info("Sorted Restaurant by cuisine and name");
        return all;
    }

    public List<Restaurant> sortByLocationDesc() {
        List<Restaurant> all = getAll();
        all.sort(Comparator.comparing(Restaurant::getLocation).reversed());
        logger.info("Sorted Restaurant by location (desc)");
        return all;
    }
}