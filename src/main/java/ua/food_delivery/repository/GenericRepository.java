package ua.food_delivery.repository;

import ua.food_delivery.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class GenericRepository<T> {

    private static final Logger logger = LoggerUtil.getLogger();
    private final List<T> items;
    private final IdentityExtractor<T> identityExtractor;
    private final String entityType; // Entity name for logging


    public GenericRepository(IdentityExtractor<T> identityExtractor, String entityType) {
        this.items = new ArrayList<>();
        this.identityExtractor = identityExtractor;
        this.entityType = entityType;
        logger.info("Repository created for " + entityType);
    }

    public boolean add(T item) {
        if (item == null) {
            logger.warning("Attempted to add a null " + entityType);
            return false;
        }

        String identity = identityExtractor.extractIdentity(item);
        if (findByIdentity(identity).isPresent()) {
            logger.warning(entityType + " with identity '" + identity + "' already exists. Add skipped.");
            return false;
        }

        boolean added = items.add(item);
        if (added) {
            logger.info("Added " + entityType + " with identity '" + identity + "': " + item);
        }
        return added;
    }

    public boolean remove(T item) {
        if (item == null) {
            logger.warning("Attempted to remove a null " + entityType);
            return false;
        }

        boolean removed = items.remove(item); // Uses .equals()

        if (removed) {
            logger.info(entityType + " with identity '"
                    + identityExtractor.extractIdentity(item) + "' successfully removed.");
        } else {
            logger.warning("Failed to remove " + entityType + ": " + item);
        }
        return removed;
    }

    public List<T> getAll() {
        logger.info("Getting all " + items.size() + " " + entityType + " items");
        // Return a copy to prevent external modification of the original list
        return new ArrayList<>(items);
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null || identity.trim().isEmpty()) {
            logger.warning("Attempted to find " + entityType + " with null or empty identity.");
            return Optional.empty();
        }

        // This is an O(n) operation, as in your example
        Optional<T> result = items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();

        if (result.isPresent()) {
            logger.info("Found " + entityType + " with identity '" + identity + "'.");
        } else {
            logger.info(entityType + " with identity '" + identity + "' not found.");
        }
        return result;
    }
}