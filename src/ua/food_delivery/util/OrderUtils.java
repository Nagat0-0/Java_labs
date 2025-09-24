package ua.food_delivery.util;

import ua.food_delivery.model.MenuItem;

import java.util.List;

public class OrderUtils {

    private OrderUtils() {
    }

    public static boolean isValidItems(List<MenuItem> items) {
        return ValidationHelper.isValidList(items, 1);
    }
}
