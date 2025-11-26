package ua.food_delivery.repository;

import ua.food_delivery.model.CuisineType;
import ua.food_delivery.model.Restaurant;
import ua.food_delivery.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RestaurantRepository extends GenericRepository<Restaurant> {
    private static final Logger logger = LoggerUtil.getLogger();

    public RestaurantRepository() {
        super(Restaurant::getName, "Restaurant");
    }

    public List<Restaurant> sortByCuisineAndName() {
        List<Restaurant> all = getAll();
        all.sort(Comparator.comparing(Restaurant::getCuisineType).thenComparing(Restaurant::getName));
        logger.info("Sorted Restaurant by cuisine and name");
        return all;
    }

    public List<Restaurant> sortByLocationDesc() {
        List<Restaurant> all = getAll();
        all.sort(Comparator.comparing(Restaurant::getLocation).reversed());
        logger.info("Sorted Restaurant by location (desc)");
        return all;
    }

    public List<Restaurant> findByCuisine(CuisineType type) {
        List<Restaurant> results = items.stream()
                .filter(r -> r.getCuisineType() == type)
                .collect(Collectors.toList());
        logger.info("Found " + results.size() + " restaurants with cuisine " + type);
        return results;
    }

    public Map<CuisineType, List<Restaurant>> groupByCuisine() {
        Map<CuisineType, List<Restaurant>> grouped = items.stream()
                .collect(Collectors.groupingBy(Restaurant::getCuisineType));

        logger.info("Grouped restaurants by cuisine: " + grouped.size() + " groups");
        return grouped;
    }
}