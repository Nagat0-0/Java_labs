package ua.food_delivery.repository;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.LoggerUtil;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class GenericRepository<T> {

    private static final Logger logger = LoggerUtil.getLogger();

    protected final List<T> items;
    private final IdentityExtractor<T> identityExtractor;
    protected final String entityType;

    public GenericRepository(IdentityExtractor<T> identityExtractor, String entityType) {
        this.items = new CopyOnWriteArrayList<>();
        this.identityExtractor = identityExtractor;
        this.entityType = entityType;
        logger.info("Created thread-safe repository for " + entityType);
    }

    public synchronized boolean add(T item) {
        if (item == null) {
            throw new InvalidDataException(entityType + " cannot be null");
        }

        String identity = identityExtractor.extractIdentity(item);

        if (findByIdentity(identity).isPresent()) {
            String errorMsg = String.format("%s already exists with identity: %s", entityType, identity);
            logger.severe(errorMsg);
            return false;
        }

        items.add(item);
        logger.info("Added " + entityType + ": " + identity);
        return true;
    }

    public int addAll(Collection<T> newItems) {
        if (newItems == null || newItems.isEmpty()) {
            return 0;
        }

        int addedCount = 0;
        for (T item : newItems) {
            try {
                if (add(item)) {
                    addedCount++;
                }
            } catch (Exception e) {
                logger.warning("Skipping duplicate or invalid: " + e.getMessage());
            }
        }

        logger.info("Bulk added " + addedCount + " of " + newItems.size() + " items to " + entityType);
        return addedCount;
    }

    public synchronized boolean remove(T item) {
        if (item == null) {
            logger.warning("Attempted to remove null " + entityType);
            return false;
        }

        boolean removed = items.remove(item);
        if (removed) {
            logger.info("Removed " + entityType + ": " + item);
        } else {
            logger.warning("Failed to remove " + entityType + ": " + item);
        }
        return removed;
    }

    public synchronized boolean removeByIdentity(String identity) {
        if (identity == null) {
            logger.warning("Attempted to remove " + entityType + " with null identity");
            return false;
        }

        Optional<T> itemToRemove = findByIdentity(identity);

        if (itemToRemove.isPresent()) {
            boolean removed = items.remove(itemToRemove.get());
            if (removed) {
                logger.info("Removed " + entityType + " by identity: " + identity);
            }
            return removed;
        } else {
            logger.warning("No " + entityType + " found with identity: " + identity + " to remove");
            return false;
        }
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null) {
            logger.warning("Attempted to find " + entityType + " with null identity");
            return Optional.empty();
        }

        Optional<T> result = items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();


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

    public synchronized void clear() {
        int sizeBefore = items.size();
        items.clear();
        logger.info("Cleared repository. Removed " + sizeBefore + " " + entityType + " items");
    }

    public synchronized void sortByIdentity(boolean asc) {
        List<T> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(identityExtractor::extractIdentity));
        if (!asc) {
            Collections.reverse(sorted);
        }
        items.clear();
        items.addAll(sorted);
        logger.info("Sorted " + entityType + " by identity in " + (asc ? "ascending" : "descending") + " order");
    }

    public void sortByIdentity(String order) {
        boolean asc = !"desc".equalsIgnoreCase(order);
        sortByIdentity(asc);
    }
}