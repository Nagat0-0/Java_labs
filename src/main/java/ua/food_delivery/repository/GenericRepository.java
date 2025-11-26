package ua.food_delivery.repository;

import ua.food_delivery.util.LoggerUtil;

import java.util.*;
import java.util.logging.Logger;

public class GenericRepository<T> {

    private static final Logger logger = LoggerUtil.getLogger();

    protected final List<T> items;
    private final IdentityExtractor<T> identityExtractor;
    private final String entityType;

    public GenericRepository(IdentityExtractor<T> identityExtractor, String entityType) {
        this.items = new ArrayList<>();
        this.identityExtractor = identityExtractor;
        this.entityType = entityType;
        logger.info("Created repository for " + entityType);
    }

    public boolean add(T item) {
        if (item == null) {
            logger.warning("Attempted to add null " + entityType);
            return false;
        }

        String identity = identityExtractor.extractIdentity(item);
        if (findByIdentity(identity).isPresent()) {
            logger.warning("Cannot add " + entityType + " - already exists with identity: " + identity);
            return false;
        }

        boolean added = items.add(item);
        if (added) {
            logger.info("Added " + entityType + ": " + identity);
        }
        return added;
    }

    public boolean remove(T item) {
        if (item == null) {
            logger.warning("Attempted to remove null " + entityType);
            return false;
        }

        boolean removed = items.remove(item);
        String identity = identityExtractor.extractIdentity(item);

        if (removed) {
            logger.info("Removed " + entityType + ": " + identity);
        } else {
            logger.warning("Failed to remove " + entityType + ": " + identity);
        }
        return removed;
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null) {
            logger.warning("Attempted to find " + entityType + " with null identity");
            return Optional.empty();
        }

        Optional<T> result = items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();

        if (result.isPresent()) {
            logger.info("Found " + entityType + " with identity: " + identity);
        } else {
            logger.info("No " + entityType + " found with identity: " + identity);
        }

        return result;
    }

    public List<T> getAll() {
        logger.info("Retrieved all " + entityType + " items. Count: " + items.size());
        return new ArrayList<>(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
        logger.info("Cleared repository " + entityType);
    }

    public void sortByIdentity(boolean asc) {
        items.sort(Comparator.comparing(identityExtractor::extractIdentity));
        if (!asc) {
            Collections.reverse(items);
        }
        logger.info("Sorted " + entityType + " by identity in " + (asc ? "ascending" : "descending") + " order");
    }

    public void sortByIdentity(String order) {
        boolean asc = !"desc".equalsIgnoreCase(order);
        sortByIdentity(asc);
    }
}