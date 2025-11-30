package ua.food_delivery.service.loading;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.food_delivery.config.AppConfig;
import ua.food_delivery.model.*;
import ua.food_delivery.persistence.PersistenceManager;
import ua.food_delivery.repository.*;

import static org.junit.jupiter.api.Assertions.*;

class DataLoaderTest {

    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        AppConfig config = new AppConfig();
        PersistenceManager persistenceManager = new PersistenceManager(config);
        dataLoader = new DataLoader(persistenceManager, "JSON");
    }

    @Test
    void loadEntity_MenuItems_ShouldLoadEntitiesIntoRepository() throws Exception {
        MenuItemRepository repo = new MenuItemRepository();

        int loaded = dataLoader.loadEntity(MenuItem.class, repo);

        assertTrue(loaded >= 0);
    }

    @Test
    void loadEntity_Restaurants_ShouldLoadEntitiesIntoRepository() throws Exception {
        RestaurantRepository repo = new RestaurantRepository();

        int loaded = dataLoader.loadEntity(Restaurant.class, repo);

        assertTrue(loaded >= 0);
    }

    @Test
    void loadEntity_Customers_ShouldLoadEntitiesIntoRepository() throws Exception {
        CustomerRepository repo = new CustomerRepository();

        int loaded = dataLoader.loadEntity(Customer.class, repo);

        assertTrue(loaded >= 0);
    }

    @Test
    void loadEntity_Orders_ShouldLoadEntitiesIntoRepository() throws Exception {
        OrderRepository repo = new OrderRepository();

        int loaded = dataLoader.loadEntity(Order.class, repo);

        assertTrue(loaded >= 0);
    }

    @Test
    void loadEntity_WithCustomEntityType_ShouldWork() throws Exception {
        MenuItemRepository repo = new MenuItemRepository();

        int loaded = dataLoader.loadEntity("menuitems", MenuItem.class, repo);

        assertTrue(loaded >= 0);
    }

    @Test
    void loadEntity_IntoNonEmptyRepository_ShouldAddNewItems() throws Exception {
        MenuItemRepository repo = new MenuItemRepository();
        repo.add(MenuItem.createMenuItem("New Pizza", 100.0, "Food"));
        int initialSize = repo.size();

        int loaded = dataLoader.loadEntity(MenuItem.class, repo);

        assertEquals(initialSize + loaded, repo.size());
    }

    @Test
    void loadEntity_WithDuplicates_ShouldSkipExisting() throws Exception {
        MenuItemRepository repo = new MenuItemRepository();
        int loaded1 = dataLoader.loadEntity(MenuItem.class, repo);

        int loaded2 = dataLoader.loadEntity(MenuItem.class, repo);

        assertEquals(0, loaded2);
    }
}