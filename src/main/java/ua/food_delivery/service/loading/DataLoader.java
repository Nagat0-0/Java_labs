package ua.food_delivery.service.loading;

import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.persistence.PersistenceManager;
import ua.food_delivery.repository.*;
import ua.food_delivery.service.LoadResult;
import ua.food_delivery.util.LoggerUtil;

import java.util.List;
import java.util.logging.Logger;

public class DataLoader {

    private static final Logger logger = LoggerUtil.getLogger();

    private final PersistenceManager persistenceManager;
    private final String format;

    public DataLoader(PersistenceManager persistenceManager, String format) {
        this.persistenceManager = persistenceManager;
        this.format = format;
        logger.info("DataLoader initialized with format: " + format);
    }

    public DataLoader(PersistenceManager persistenceManager) {
        this(persistenceManager, "JSON");
    }

    public <T> int loadEntity(Class<T> clazz, GenericRepository<T> repository) throws DataSerializationException {
        String entityType = clazz.getSimpleName().toLowerCase() + "s";
        return loadEntity(entityType, clazz, repository);
    }

    public <T> int loadEntity(String entityType, Class<T> clazz, GenericRepository<T> repository) throws DataSerializationException {
        List<T> items = persistenceManager.load(entityType, clazz, format);
        return repository.addAll(items);
    }

    public LoadResult load(
            CustomerRepository customerRepository,
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository,
            OrderRepository orderRepository,
            LoadingStrategy strategy) {

        logger.info("Loading data using strategy: " + strategy.getClass().getSimpleName());

        return strategy.load(
                customerRepository,
                restaurantRepository,
                menuItemRepository,
                orderRepository,
                this
        );
    }
}