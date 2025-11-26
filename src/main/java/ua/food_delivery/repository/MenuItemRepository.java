package ua.food_delivery.repository;

import ua.food_delivery.model.MenuItem;
import ua.food_delivery.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MenuItemRepository extends GenericRepository<MenuItem> {
    private static final Logger logger = LoggerUtil.getLogger();

    public MenuItemRepository() {
        super(MenuItem::getName, "MenuItem");
    }

    public List<MenuItem> sortByPriceAsc() {
        List<MenuItem> all = getAll();
        all.sort(Comparator.comparingDouble(MenuItem::getPrice));
        logger.info("Sorted MenuItem by price (asc)");
        return all;
    }

    public List<MenuItem> sortByCategoryAndPriceDesc() {
        List<MenuItem> all = getAll();
        all.sort(Comparator.comparing(MenuItem::getCategory)
                .thenComparing(Comparator.comparingDouble(MenuItem::getPrice).reversed()));
        logger.info("Sorted MenuItem by category and price (desc)");
        return all;
    }

    public List<MenuItem> findByPriceRange(double min, double max) {
        List<MenuItem> results = items.stream()
                .filter(m -> m.getPrice() >= min && m.getPrice() <= max)
                .collect(Collectors.toList());
        logger.info(String.format("Found %d items with price between %.2f and %.2f", results.size(), min, max));
        return results;
    }

    public Optional<MenuItem> getMostExpensiveItem() {
        Optional<MenuItem> result = items.stream()
                .max(Comparator.comparingDouble(MenuItem::getPrice));

        result.ifPresent(m -> logger.info("Most expensive item: " + m.getName() + " (" + m.getPrice() + ")"));
        return result;
    }
}