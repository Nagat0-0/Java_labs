package ua.food_delivery.repository;

import ua.food_delivery.model.MenuItem;
import ua.food_delivery.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

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
}