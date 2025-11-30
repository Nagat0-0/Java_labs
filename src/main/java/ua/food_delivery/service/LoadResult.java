package ua.food_delivery.service;

public record LoadResult(
        int customers,
        int restaurants,
        int menuItems,
        int orders,
        long durationMs
) {
    public int getTotalItems() {
        return customers + restaurants + menuItems + orders;
    }

    @Override
    public String toString() {
        return String.format("""
                === Load Result ===
                Total items: %d
                - Customers:   %d
                - Restaurants: %d
                - MenuItems:   %d
                - Orders:      %d
                Duration:    %d ms
                """, getTotalItems(), customers, restaurants, menuItems, orders, durationMs);
    }
}