package ua.food_delivery.service.loading;

import ua.food_delivery.repository.*;
import ua.food_delivery.service.LoadResult;

@FunctionalInterface
public interface LoadingStrategy {

    LoadResult load(
            CustomerRepository customerRepository,
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository,
            OrderRepository orderRepository,
            DataLoader dataLoader
    );
}